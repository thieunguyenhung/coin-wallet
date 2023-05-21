package io.github.thieunguyenhung.coinwallet.v2.writer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.thieunguyenhung.coinwallet.global.Constants
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toStringBy
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
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
import java.time.Instant
import javax.servlet.ServletContext

@IntegrationTest
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
            .baseUri("http://localhost:$port${servletContext.contextPath}${Constants.REQUEST_MAPPING_API_PATH_V2}")
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(body))
            .`when`()
            .post(Constants.POST_DEPOSIT_PATH)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("datetime", CoreMatchers.equalTo(body.datetime!! toStringBy pattern))
            .extract().body().jsonPath().getObject("", WalletDtoResponse::class.java)

        val savedEntity = readerRepository.findById(actualResponse.id)

        Assertions.assertThat(savedEntity).isPresent
        Assertions.assertThat(savedEntity.get()).extracting("amount").isEqualTo(body.amount)
    }
}