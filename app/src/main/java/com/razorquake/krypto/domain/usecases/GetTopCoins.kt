package com.razorquake.krypto.domain.usecases

import com.razorquake.krypto.domain.repo.CoinRepository

class GetTopCoins(
    private val repository: CoinRepository,
) {
    suspend operator fun invoke() = repository.getTopCoins()
}