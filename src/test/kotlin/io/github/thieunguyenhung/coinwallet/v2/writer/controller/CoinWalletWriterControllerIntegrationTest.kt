package io.github.thieunguyenhung.coinwallet.v2.writer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_DEPOSIT_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_HISTORY_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V2
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toStringBy
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.utils.IntegrationTest
import io.github.thieunguyenhung.coinwallet.v2.reader.repository.WalletReaderRepository
import io.github.thieunguyenhung.coinwallet.v2.writer.repository.WalletWriterRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext
import org.springframework.test.context.TestPropertySource
import java.math.BigDecimal
import java.time.Instant
import javax.servlet.ServletContext

@IntegrationTest
@TestPropertySource(properties = ["wallet.enabled.reader=false"])
class CoinWalletWriterControllerIntegrationTest {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    @Autowired
    private lateinit var writerRepository: WalletWriterRepository

    @Autowired
    private lateinit var readerRepository: WalletReaderRepository

    @Autowired
    private lateinit var server: ServletWebServerApplicationContext

    @Autowired
    private lateinit var servletContext: ServletContext

    private lateinit var objectMapper: ObjectMapper

    private var port = 0

    @BeforeEach
    fun setup() {
        port = server.webServer.port

        objectMapper = ObjectMapper().registerModule(JavaTimeModule()).registerKotlinModule()
    }

    @AfterEach
    fun tearDown() {
        writerRepository.deleteAll()
    }

    @Test
    fun `SHOULD save a new record for GIVEN valid DepositRequest`() {
        val body = DepositRequest(datetime = Instant.now(), amount = BigDecimal(1.1))

        val actualResponse = RestAssured.given()
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V2")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .post(POST_DEPOSIT_PATH)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("datetime", equalTo(body.datetime!! toStringBy pattern))
            .extract().body().jsonPath().getObject("", WalletDtoResponse::class.java)

        val savedEntity = readerRepository.findById(actualResponse.id)

        assertThat(savedEntity).isPresent
        assertThat(savedEntity.get()).extracting("amount").isEqualTo(body.amount)
    }

    @Test
    fun `SHOULD return NOT_FOUND since ReaderController has been disabled`() {
        val body = HistoryLogRequest(
            startDatetime = "2023-05-01T00:00:05+07:00" toInstantBy pattern,
            endDatetime = "2023-05-09T00:00:05+07:00" toInstantBy pattern
        )

        RestAssured.given()
            .baseUri("http://localhost:$port${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V2")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .queryParam("page", 0)
            .queryParam("size", 4)
            .post(POST_HISTORY_PATH)
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("status", equalTo(HttpStatus.SC_NOT_FOUND))
            .body("error", equalTo(org.springframework.http.HttpStatus.NOT_FOUND.reasonPhrase))
            .body("path", equalTo("${servletContext.contextPath}$REQUEST_MAPPING_API_PATH_V2$POST_HISTORY_PATH"))
    }
}
