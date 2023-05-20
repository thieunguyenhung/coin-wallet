package io.github.thieunguyenhung.coinwallet.service

import io.github.thieunguyenhung.coinwallet.entity.Wallet
import io.github.thieunguyenhung.coinwallet.mapper.WalletMapper
import io.github.thieunguyenhung.coinwallet.model.DepositRequest
import io.github.thieunguyenhung.coinwallet.model.HistoryLogRequest
import io.github.thieunguyenhung.coinwallet.model.WalletDtoResponse
import io.github.thieunguyenhung.coinwallet.repository.WalletRepository
import io.github.thieunguyenhung.coinwallet.utils.AnyMatcher.Companion.any
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class WalletServiceTest {
    @Mock
    private lateinit var walletRepository: WalletRepository

    @Mock
    private lateinit var walletMapper: WalletMapper

    @InjectMocks
    private lateinit var walletService: WalletService

    @Test
    fun `SHOULD perform save entity to repository successfully for GIVEN DepositRequest`() {
        val request = DepositRequest(
            amount = BigDecimal("0.7"),
            datetime = Instant.now()
        )
        val mockEntity = Wallet(
            id = UUID.randomUUID(),
            amount = request.amount,
            createdAt = request.datetime
        )

        whenever(walletMapper.mapToWalletEntity(any(DepositRequest::class.java))).thenReturn(mockEntity)
        whenever(walletRepository.save(any(Wallet::class.java))).thenReturn(mockEntity)
        whenever(walletMapper.mapToWalletDtoResponse(any(Wallet::class.java))).thenReturn(mockEntity.toWalletDtoResponse())

        val actual = walletService.depositCoin(request)

        assertThat(actual).isEqualTo(mockEntity.toWalletDtoResponse())

        verify(walletRepository, times(1)).save(mockEntity)
    }

    @Test
    fun `SHOULD return Page of WalletDtoResponse items for GIVEN HistoryLogRequest and Pageable`() {
        val mockWallets = listOf(
            Wallet(
                id = UUID.randomUUID(),
                amount = BigDecimal("0.7"),
                createdAt = Instant.now()
            ),
            Wallet(
                id = UUID.randomUUID(),
                amount = BigDecimal("1.8"),
                createdAt = Instant.now()
            )
        )
        val pageRequest = PageRequest.of(0, 2)

        whenever(walletMapper.mapToWalletDtoResponse(mockWallets.first())).thenReturn(
            mockWallets.first().toWalletDtoResponse()
        )
        whenever(walletMapper.mapToWalletDtoResponse(mockWallets.last())).thenReturn(
            mockWallets.last().toWalletDtoResponse()
        )
        whenever(
            walletRepository.findAllByCreatedAtBetween(
                any(Instant::class.java),
                any(Instant::class.java),
                any(Pageable::class.java)
            )
        ).thenReturn(PageImpl(mockWallets, pageRequest, mockWallets.size.toLong()))

        val request = HistoryLogRequest(
            startDatetime = Instant.now(),
            endDatetime = Instant.now().plus(1, ChronoUnit.DAYS)
        )

        val actual = walletService.getHistoryBetween(request, pageRequest)
        assertThat(actual.size).isEqualTo(2)
        assertThat(actual.content).containsExactlyInAnyOrderElementsOf(mockWallets.map { it.toWalletDtoResponse() })

        verify(walletRepository, times(1)).findAllByCreatedAtBetween(
            request.startDatetime!!,
            request.endDatetime!!,
            pageRequest
        )
    }

    private fun Wallet.toWalletDtoResponse(): WalletDtoResponse = WalletDtoResponse(
        id = this.id!!,
        amount = this.amount!!,
        datetime = this.createdAt!!
    )
}
