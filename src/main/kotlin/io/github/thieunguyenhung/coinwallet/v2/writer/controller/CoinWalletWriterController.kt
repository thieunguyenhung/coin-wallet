package io.github.thieunguyenhung.coinwallet.v2.writer.controller

import io.github.thieunguyenhung.coinwallet.global.Constants
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.v2.writer.service.WalletWriterService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(Constants.REQUEST_MAPPING_API_PATH_V2)
@ConditionalOnProperty(name = ["wallet.enabled.writer"], havingValue = "true")
class CoinWalletWriterController(
    private val walletWriterService: WalletWriterService
) {
    @PostMapping(Constants.POST_DEPOSIT_PATH)
    fun deposit(@Valid @RequestBody depositRequest: DepositRequest): ResponseEntity<WalletDtoResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(walletWriterService.depositCoin(depositRequest))
    }
}
