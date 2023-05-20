package io.github.thieunguyenhung.coinwallet.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.thieunguyenhung.coinwallet.helper.JsonSerializerForJavaInstant
import java.math.BigDecimal
import java.time.Instant
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero

data class DepositRequest(
    @field:NotNull
    @field:PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonSerialize(using = JsonSerializerForJavaInstant::class)
    val datetime: Instant?,

    @field:NotNull
    @field:PositiveOrZero
    val amount: BigDecimal?
)
