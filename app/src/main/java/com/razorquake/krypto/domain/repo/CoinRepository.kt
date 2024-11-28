package com.razorquake.krypto.domain.repo

import androidx.paging.PagingData
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.data.remote.dto.CoinMarketChartResponse
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getTopCoins(): Flow<PagingData<Coin>>

    fun getAllCoins(): Flow<List<Coin>>

    suspend fun getCoinMarketChart(coinId: String, days: String): CoinMarketChartResponse

    suspend fun insertCoin(coin: Coin)

    suspend fun updateCoin(coin: Coin)

    suspend fun deleteCoin(id: String)

    suspend fun getCoin(id: String): Coin?
}