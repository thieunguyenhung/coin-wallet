package io.github.thieunguyenhung.coinwallet.v2.reader.controller

import io.github.thieunguyenhung.coinwallet.global.Constants
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.v2.reader.service.WalletReaderService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
@RequestMapping(Constants.REQUEST_MAPPING_API_PATH_V2)
@ConditionalOnProperty(name = ["wallet.enabled.reader"], havingValue = "true")
class CoinWalletReaderController(
    private val walletReaderService: WalletReaderService
) {
    @PostMapping(Constants.POST_HISTORY_PATH)
    fun getHistoryLog(
        @Valid @RequestBody historyLogRequest: HistoryLogRequest,
        pageable: Pageable
    ): ResponseEntity<Page<WalletDtoResponse>> {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(walletReaderService.getHistoryBetween(historyLogRequest, pageable))
    }
}
