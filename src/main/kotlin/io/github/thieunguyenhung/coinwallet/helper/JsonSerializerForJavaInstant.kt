package io.github.thieunguyenhung.coinwallet.helper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class JsonSerializerForJavaInstant : JsonSerializer<Instant>() {
    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(
            DateTimeFormatter.ofPattern(if (this::pattern.isInitialized) pattern else "yyyy-MM-dd'T'HH:mm:ssXXX")
                .withZone(ZoneId.systemDefault())
                .format(value)
        )
    }
}

class JavaInstantHelper {
    companion object {
        infix fun String.toInstantBy(pattern: String): Instant = Instant.from(
            ZonedDateTime.parse(
                this,
                DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
            )
        )

        infix fun Instant.toStringBy(pattern: String): String =
            DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(this)
    }
}
