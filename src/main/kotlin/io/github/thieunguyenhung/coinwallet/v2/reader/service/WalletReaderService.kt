package io.github.thieunguyenhung.coinwallet.v2.reader.service

import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.v2.reader.repository.WalletReaderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class WalletReaderService(
    private val walletReaderRepository: WalletReaderRepository,
    private val walletMapper: WalletMapper
) {
    fun getHistoryBetween(historyLogRequest: HistoryLogRequest, pageable: Pageable): Page<WalletDtoResponse> {
        val wallets = walletReaderRepository.findAllByCreatedAtBetween(
            historyLogRequest.startDatetime!!,
            historyLogRequest.endDatetime!!,
            pageable
        )
        return wallets.map { walletMapper.mapToWalletDtoResponse(it) }
    }
}