package com.razorquake.krypto.domain.usecases

import com.razorquake.krypto.domain.repo.CoinRepository

class GetCoin(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(id: String) = coinRepository.getCoin(id)
}