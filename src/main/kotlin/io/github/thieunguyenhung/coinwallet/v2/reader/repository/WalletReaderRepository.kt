package io.github.thieunguyenhung.coinwallet.v2.reader.repository

import io.github.thieunguyenhung.coinwallet.entity.Wallet
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface WalletReaderRepository : JpaRepository<Wallet, UUID> {
    fun findAllByCreatedAtBetween(
        startDatetime: Instant,
        endDatetime: Instant,
        pageable: Pageable
    ): Page<Wallet>
}
