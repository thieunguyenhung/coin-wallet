package io.github.thieunguyenhung.coinwallet.service

import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.repository.WalletRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val walletMapper: WalletMapper
) {
    fun depositCoin(depositRequest: DepositRequest): WalletDtoResponse {
        val savedWalletEntity = walletRepository.save(walletMapper.mapToWalletEntity(depositRequest))
        return walletMapper.mapToWalletDtoResponse(savedWalletEntity)
    }

    fun getHistoryBetween(historyLogRequest: HistoryLogRequest, pageable: Pageable): Page<WalletDtoResponse> {
        val wallets = walletRepository.findAllByCreatedAtBetween(
            historyLogRequest.startDatetime!!,
            historyLogRequest.endDatetime!!,
            pageable
        )
        return wallets.map { walletMapper.mapToWalletDtoResponse(it) }
    }
}
