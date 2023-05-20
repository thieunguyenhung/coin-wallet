package io.github.thieunguyenhung.coinwallet.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.thieunguyenhung.coinwallet.exception.constraint.ValidateHistoryLogRequest
import io.github.thieunguyenhung.coinwallet.helper.JsonSerializerForJavaInstant
import java.time.Instant
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent

@ValidateHistoryLogRequest
data class HistoryLogRequest(
    @field:NotNull
    @field:PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonSerialize(using = JsonSerializerForJavaInstant::class)
    val startDatetime: Instant?,

    @field:NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonSerialize(using = JsonSerializerForJavaInstant::class)
    val endDatetime: Instant?
)
