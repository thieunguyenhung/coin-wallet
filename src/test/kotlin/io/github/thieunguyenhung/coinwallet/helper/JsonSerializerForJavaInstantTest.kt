package io.github.thieunguyenhung.coinwallet.helper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@Disabled("For testing on GitHub Action")
@SpringBootTest
@ActiveProfiles(profiles = ["local"])
class JsonSerializerForJavaInstantTest {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(SimpleModule().addSerializer(Instant::class.java, JsonSerializerForJavaInstant()))
    }

    @Test
    fun `SHOULD serialize Java Instant to a proper formatted String`() {
        val expected = "2023-05-02T14:45:05+07:00"

        val actual = objectMapper.writeValueAsString(expected toInstantBy pattern)

        assertThat(actual).contains(expected)
    }
}
