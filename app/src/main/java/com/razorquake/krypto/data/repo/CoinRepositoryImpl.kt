package com.razorquake.krypto.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.razorquake.krypto.data.local.CoinDao
import com.razorquake.krypto.data.remote.ApiService
import com.razorquake.krypto.data.remote.CoinPagingSource
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.data.remote.dto.CoinMarketChartResponse
import com.razorquake.krypto.domain.repo.CoinRepository
import kotlinx.coroutines.flow.Flow

class CoinRepositoryImpl(
    private val api: ApiService,
    private val coinDao: CoinDao
) : CoinRepository {
    override suspend fun getTopCoins(): Flow<PagingData<Coin>>{
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                CoinPagingSource(
                    apiService = api
                )
            }
        ).flow
    }

    override fun getAllCoins(): Flow<List<Coin>> = coinDao.getCoins()

    override suspend fun getCoinMarketChart(coinId: String, days: String): CoinMarketChartResponse {
        return api.getCoinMarketChart(coinId = coinId, days = days)
    }

    override suspend fun insertCoin(coin: Coin) = coinDao.insertCoin(coin)

    override suspend fun updateCoin(coin: Coin) = coinDao.updateCoin(coin)
    override suspend fun deleteCoin(id: String) = coinDao.deleteCoin(id)

    override suspend fun getCoin(id: String): Coin? = coinDao.getCoin(id)
}