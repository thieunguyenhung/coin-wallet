package io.github.thieunguyenhung.coinwallet.repository

import io.github.thieunguyenhung.coinwallet.entity.Wallet
import io.github.thieunguyenhung.coinwallet.utils.IntegrationTest
import io.github.thieunguyenhung.coinwallet.helper.JavaInstantHelper.Companion.toInstantBy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.temporal.ChronoUnit

@IntegrationTest
class WalletRepositoryIntegrationTest {
    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Value("\${wallet.iso_offset_date_time}")
    private lateinit var pattern: String

    @AfterEach
    fun tearDown() {
        walletRepository.deleteAll()
    }

    @Test
    fun `SHOULD allow to save new record`() {
        assertThat(walletRepository.findAll()).isEmpty()

        val wallet = walletRepository.save(createWallet("2023-05-02T14:45:05+07:00"))

        val savedEntities = walletRepository.findAll()
        assertThat(savedEntities).hasSize(1)
        assertThat(savedEntities.first()).isEqualTo(wallet)
    }

    @Test
    fun `SHOULD return all records between given startDatetime and endDatetime`() {
        val expectedEntities = walletRepository.saveAll(
            listOf(
                createWallet("2023-05-02T14:45:05+07:00"),
                createWallet("2023-05-02T15:45:05+07:00"),
                createWallet("2023-05-03T10:45:05+07:00"),
                createWallet("2023-05-03T11:45:05+07:00")
            )
        )
        walletRepository.saveAll(
            listOf(
                createWallet("2023-05-03T08:45:05+07:00"),
                createWallet("2022-05-10T14:45:05+07:00")
            )
        )

        val startDatetime = "2023-05-01T00:00:05+07:00" toInstantBy pattern
        val actualResult = walletRepository.findAllByCreatedAtBetween(
            startDatetime,
            startDatetime.plus(5, ChronoUnit.DAYS),
            PageRequest.of(0, 4)
        )

        assertThat(actualResult.size).isEqualTo(4)
        assertThat(actualResult.content).containsExactlyInAnyOrderElementsOf(expectedEntities)
    }

    private fun createWallet(createdAt: String): Wallet = Wallet(
        amount = BigDecimal("1.8"),
        createdAt = createdAt toInstantBy pattern
    )
}
