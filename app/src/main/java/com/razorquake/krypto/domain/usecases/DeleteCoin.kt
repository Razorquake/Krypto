package com.razorquake.krypto.domain.usecases

import com.razorquake.krypto.domain.repo.CoinRepository

class DeleteCoin (
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(id : String){
        coinRepository.deleteCoin(id)
    }
}