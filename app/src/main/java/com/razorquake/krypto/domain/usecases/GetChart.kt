package com.razorquake.krypto.domain.usecases

import com.razorquake.krypto.data.remote.mappers.mapMarketChartDataPointList
import com.razorquake.krypto.domain.repo.CoinRepository

class GetChart(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(id : String, days : String) = mapMarketChartDataPointList(coinRepository.getCoinMarketChart(id, days))
}