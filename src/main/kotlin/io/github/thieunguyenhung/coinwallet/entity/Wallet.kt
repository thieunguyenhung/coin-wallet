package io.github.thieunguyenhung.coinwallet.entity

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Wallet(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column
    val amount: BigDecimal? = null,

    @Column
    var createdAt: Instant? = null
)
