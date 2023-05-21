package io.github.thieunguyenhung.coinwallet.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.thieunguyenhung.coinwallet.entity.Wallet
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_DEPOSIT_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_HISTORY_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V1
import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.repository.WalletRepository
import io.github.thieunguyenhung.coinwallet.utils.IntegrationTest
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toStringBy
import io.github.thieunguyenhung.coinwallet.utils.SpringRestDocumentationConfiguration
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.restdocs.RestDocumentationContextProvider
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.servlet.ServletContext

@IntegrationTest
class CoinWalletControllerIntegrationTest {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Autowired
    private lateinit var server: ServletWebServerApplicationContext

    @Autowired
    private lateinit var servletContext: ServletContext

    @Autowired
    private lateinit var walletMapper: WalletMapper

    private lateinit var documentationSpec: RequestSpecification

    private lateinit var objectMapper: ObjectMapper

    private var port = 0

    @BeforeEach
    fun setup(restDocumentation: RestDocumentationContextProvider) {
        port = server.webServer.port

        objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerKotlinModule()

        documentationSpec = SpringRestDocumentationConfiguration.prepareDocumentationSpecification(restDocumentation)
    }

    @AfterEach
    fun tearDown() {
        walletRepository.deleteAll()
    }

    @Test
    fun `SHOULD save a new record for GIVEN valid DepositRequest`() {
        val body = DepositRequest(datetime = Instant.now(), amount = BigDecimal(1.1))

        val actualResponse = RestAssured.given(documentationSpec)
            .filter(CoinWalletControllerDocHelper.POST_DEPOSIT)
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V1")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .post(POST_DEPOSIT_PATH)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("datetime", equalTo(body.datetime!! toStringBy pattern))
            .extract().body().jsonPath().getObject("", WalletDtoResponse::class.java)

        val savedEntity = walletRepository.findById(actualResponse.id)

        assertThat(savedEntity).isPresent
        assertThat(savedEntity.get()).extracting("amount").isEqualTo(body.amount)
    }

    @Test
    fun `SHOULD return all records for GIVEN valid HistoryLogRequest`() {
        val expectedEntities = walletRepository.saveAll(
            listOf(
                createWallet("2023-05-02T14:45:05+07:00"),
                createWallet("2023-05-02T15:45:05+07:00"),
                createWallet("2023-05-03T10:45:05+07:00"),
                createWallet("2023-05-03T11:45:05+07:00")
            )
        )
        walletRepository.saveAll(
            listOf(
                createWallet("2023-05-03T08:45:05+07:00"),
                createWallet("2022-05-10T14:45:05+07:00")
            )
        )

        val body = HistoryLogRequest(
            startDatetime = "2023-05-01T00:00:05+07:00" toInstantBy pattern,
            endDatetime = "2023-05-09T00:00:05+07:00" toInstantBy pattern
        )

        val actualResponse = RestAssured.given(documentationSpec)
            .filter(CoinWalletControllerDocHelper.POST_HISTORY)
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V1")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .queryParam("page", 0)
            .queryParam("size", 4)
            .post(POST_HISTORY_PATH)
            .then()
            .statusCode(HttpStatus.SC_ACCEPTED)
            .body("size", equalTo(4))
            .body("totalPages", equalTo(2))
            .extract().body().jsonPath().getList("content", WalletDtoResponse::class.java)

        assertThat(actualResponse).containsExactlyInAnyOrderElementsOf(
            expectedEntities.map { walletMapper.mapToWalletDtoResponse(it) }
        )
    }

    @Test
    fun `SHOULD return BAD_REQUEST for GIVEN invalid DepositRequest`() {
        val body = DepositRequest(datetime = Instant.now().plus(1, ChronoUnit.DAYS), amount = BigDecimal(-1.1))

        RestAssured.given()
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V1")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .post(POST_DEPOSIT_PATH)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("code", equalTo(HttpStatus.SC_BAD_REQUEST))
            .body(
                "error",
                hasItems(
                    "datetime must be a date in the past or in the present",
                    "amount must be greater than or equal to 0"
                )
            )
    }

    @Test
    fun `SHOULD return BAD_REQUEST for GIVEN invalid HistoryLogRequest`() {
        val body = HistoryLogRequest(
            startDatetime = Instant.now().plus(1, ChronoUnit.DAYS),
            endDatetime = Instant.now().minus(1, ChronoUnit.DAYS)
        )

        RestAssured.given()
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V1")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .queryParam("page", 0)
            .queryParam("size", 4)
            .post(POST_HISTORY_PATH)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("code", equalTo(HttpStatus.SC_BAD_REQUEST))
            .body(
                "error",
                hasItems(
                    "historyLogRequest startDatetime must be before endDatetime",
                    "startDatetime must be a date in the past or in the present"
                )
            )
    }

    private fun createWallet(createdAt: String): Wallet = Wallet(
        amount = BigDecimal("1.8"),
        createdAt = createdAt toInstantBy pattern
    )
}
