package io.github.thieunguyenhung.coinwallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoinWalletApplication

fun main(args: Array<String>) {
    runApplication<CoinWalletApplication>(*args)
}
