package com.razorquake.krypto.di

import android.app.Application
import com.razorquake.krypto.core.utils.Constants.BASE_URL
import com.razorquake.krypto.data.local.CoinDao
import com.razorquake.krypto.data.local.CoinDatabase
import com.razorquake.krypto.data.remote.ApiService
import com.razorquake.krypto.data.repo.CoinRepositoryImpl
import com.razorquake.krypto.domain.repo.CoinRepository
import com.razorquake.krypto.domain.usecases.CoinUseCases
import com.razorquake.krypto.domain.usecases.DeleteCoin
import com.razorquake.krypto.domain.usecases.GetChart
import com.razorquake.krypto.domain.usecases.GetCoin
import com.razorquake.krypto.domain.usecases.GetTopCoins
import com.razorquake.krypto.domain.usecases.UpsertCoin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoinDB(application: Application): CoinDatabase {
        return CoinDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideCoinDao(coinDatabase: CoinDatabase) = coinDatabase.coinDao()

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepositoryImpl(
        apiService: ApiService,
        coinDao: CoinDao
    ): CoinRepository = CoinRepositoryImpl(
        api = apiService,
        coinDao = coinDao
    )

    @Provides
    @Singleton
    fun provideCoinUseCases(
        repository: CoinRepository,
    ): CoinUseCases = CoinUseCases(
        getTopCoins = GetTopCoins(repository),
        upsertCoin = UpsertCoin(repository),
        deleteCoin = DeleteCoin(repository),
        getCoin = GetCoin(repository),
        getChart = GetChart(repository)
    )
}