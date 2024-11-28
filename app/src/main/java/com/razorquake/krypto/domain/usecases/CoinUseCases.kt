package com.razorquake.krypto.domain.usecases

data class CoinUseCases(
    val getTopCoins: GetTopCoins,
    val upsertCoin: UpsertCoin,
    val deleteCoin: DeleteCoin,
    val getCoin: GetCoin,
    val getChart: GetChart
)
