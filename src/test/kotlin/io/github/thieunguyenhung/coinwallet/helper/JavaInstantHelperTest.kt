package io.github.thieunguyenhung.coinwallet.helper

import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toStringBy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Disabled("For testing on GitHub Action")
@SpringBootTest
@ActiveProfiles(profiles = ["local"])
class JavaInstantHelperTest {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    @Test
    fun `SHOULD convert String to Instant`() {
        val expected = "2023-05-02T14:45:05+07:00"
        val actual = (expected toInstantBy pattern).atZone(ZoneId.systemDefault())

        assertThat(actual.dayOfMonth).isEqualTo(2)
        assertThat(actual.monthValue).isEqualTo(5)
        assertThat(actual.year).isEqualTo(2023)
        assertThat(actual.hour).isEqualTo(14)
        assertThat(actual.minute).isEqualTo(45)
        assertThat(actual.second).isEqualTo(5)
        assertThat(actual.offset.id).isEqualTo("+07:00")
    }

    @Test
    fun `SHOULD convert Instant to String`() {
        val offsetDateTime = OffsetDateTime.of(2023, 5, 1, 7, 18, 10, 0, ZoneOffset.of("+07:00"))
        val actual = offsetDateTime.toInstant() toStringBy pattern
        val expected = offsetDateTime.format(DateTimeFormatter.ofPattern(pattern))

        assertThat(actual).isEqualTo(expected)
    }
}
