package io.github.thieunguyenhung.coinwallet.controller

import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_DEPOSIT_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.POST_HISTORY_PATH
import io.github.thieunguyenhung.coinwallet.global.Constants.Companion.REQUEST_MAPPING_API_PATH_V1
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.service.WalletService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(REQUEST_MAPPING_API_PATH_V1)
class CoinWalletController(
    private val walletService: WalletService
) {
    @PostMapping(POST_DEPOSIT_PATH)
    fun deposit(@Valid @RequestBody depositRequest: DepositRequest): ResponseEntity<WalletDtoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.depositCoin(depositRequest))
    }

    @PostMapping(POST_HISTORY_PATH)
    fun getHistoryLog(
        @Valid @RequestBody historyLogRequest: HistoryLogRequest,
        pageable: Pageable
    ): ResponseEntity<Page<WalletDtoResponse>> {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(walletService.getHistoryBetween(historyLogRequest, pageable))
    }
}
