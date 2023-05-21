package io.github.thieunguyenhung.coinwallet.v2.writer.service

import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.v2.writer.repository.WalletWriterRepository
import org.springframework.stereotype.Service

@Service
class WalletWriterService(
    private val writerRepository: WalletWriterRepository,
    private val walletMapper: WalletMapper
) {
    fun depositCoin(depositRequest: DepositRequest): WalletDtoResponse {
        val savedWalletEntity = writerRepository.save(walletMapper.mapToWalletEntity(depositRequest))
        return walletMapper.mapToWalletDtoResponse(savedWalletEntity)
    }
}
