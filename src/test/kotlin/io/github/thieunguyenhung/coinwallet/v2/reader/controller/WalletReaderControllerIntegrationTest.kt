package io.github.thieunguyenhung.coinwallet.v2.reader.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.thieunguyenhung.coinwallet.entity.Wallet
import io.github.thieunguyenhung.coinwallet.global.Constants
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.utils.IntegrationTest
import io.github.thieunguyenhung.coinwallet.v2.reader.repository.WalletReaderRepository
import io.github.thieunguyenhung.coinwallet.v2.writer.repository.WalletWriterRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import java.math.BigDecimal
import javax.servlet.ServletContext

@IntegrationTest
class WalletReaderControllerIntegrationTest {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    @Autowired
    private lateinit var readerRepository: WalletReaderRepository

    @Autowired
    private lateinit var writerRepository: WalletWriterRepository

    @Autowired
    private lateinit var server: ServletWebServerApplicationContext

    @Autowired
    private lateinit var servletContext: ServletContext

    @Autowired
    private lateinit var walletMapper: WalletMapper

    private lateinit var objectMapper: ObjectMapper

    private var port = 0

    @BeforeEach
    fun setup() {
        port = server.webServer.port

        objectMapper = ObjectMapper().registerModule(JavaTimeModule()).registerKotlinModule()
    }

    @AfterEach
    fun tearDown() {
        readerRepository.deleteAll()
    }

    @Test
    fun `SHOULD return all records for GIVEN valid HistoryLogRequest`() {
        val expectedEntities = writerRepository.saveAll(
            listOf(
                createWallet("2023-05-02T14:45:05+07:00"),
                createWallet("2023-05-02T15:45:05+07:00"),
                createWallet("2023-05-03T10:45:05+07:00"),
                createWallet("2023-05-03T11:45:05+07:00")
            )
        )
        writerRepository.saveAll(
            listOf(
                createWallet("2023-05-03T08:45:05+07:00"),
                createWallet("2022-05-10T14:45:05+07:00")
            )
        )

        val body = HistoryLogRequest(
            startDatetime = "2023-05-01T00:00:05+07:00" toInstantBy pattern,
            endDatetime = "2023-05-09T00:00:05+07:00" toInstantBy pattern
        )

        val actualResponse = RestAssured.given()
            .baseUri("http://localhost:$port${servletContext.contextPath}${Constants.REQUEST_MAPPING_API_PATH_V2}")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .queryParam("page", 0)
            .queryParam("size", 4)
            .post(Constants.POST_HISTORY_PATH)
            .then()
            .statusCode(HttpStatus.SC_ACCEPTED)
            .body("size", CoreMatchers.equalTo(4))
            .body("totalPages", CoreMatchers.equalTo(2))
            .extract().body().jsonPath().getList("content", WalletDtoResponse::class.java)

        Assertions.assertThat(actualResponse).containsExactlyInAnyOrderElementsOf(
            expectedEntities.map { walletMapper.mapToWalletDtoResponse(it) }
        )
    }

    private fun createWallet(createdAt: String): Wallet = Wallet(
        amount = BigDecimal("1.8"),
        createdAt = createdAt toInstantBy pattern
    )
}
