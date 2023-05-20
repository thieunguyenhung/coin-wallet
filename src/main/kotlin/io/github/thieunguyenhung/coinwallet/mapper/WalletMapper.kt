package io.github.thieunguyenhung.coinwallet.mapper

import io.github.thieunguyenhung.coinwallet.entity.Wallet
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import org.springframework.stereotype.Component

@Component
class WalletMapper {
    fun mapToWalletEntity(depositRequest: DepositRequest): Wallet = Wallet(
        amount = depositRequest.amount,
        createdAt = depositRequest.datetime
    )

    fun mapToWalletDtoResponse(walletEntity: Wallet): WalletDtoResponse = WalletDtoResponse(
        id = walletEntity.id!!,
        amount = walletEntity.amount!!,
        datetime = walletEntity.createdAt!!
    )
}
