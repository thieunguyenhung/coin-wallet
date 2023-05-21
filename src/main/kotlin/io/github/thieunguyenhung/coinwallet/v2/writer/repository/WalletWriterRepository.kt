package io.github.thieunguyenhung.coinwallet.v2.writer.repository

import io.github.thieunguyenhung.coinwallet.entity.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WalletWriterRepository : JpaRepository<Wallet, UUID> {}