package io.github.thieunguyenhung.coinwallet.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.thieunguyenhung.coinwallet.helper.JsonSerializerForJavaInstant
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class WalletDtoResponse(
    val id: UUID,

    val amount: BigDecimal,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonSerialize(using = JsonSerializerForJavaInstant::class)
    val datetime: Instant
)
