package com.razorquake.krypto.domain.usecases

import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.domain.repo.CoinRepository

class UpsertCoin (
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(coin: Coin){
        coinRepository.insertCoin(coin)
    }
}