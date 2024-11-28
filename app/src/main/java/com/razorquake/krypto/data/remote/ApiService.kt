package com.razorquake.krypto.data.remote

import com.razorquake.krypto.core.utils.Constants.API_KEY
import com.razorquake.krypto.data.remote.dto.CoinMarketChartResponse
import com.razorquake.krypto.data.remote.dto.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @Headers(
        "accept: application/json",
        "x-cg-demo-api-key: $API_KEY"
    )
    @GET("coins/markets")
    suspend fun getTopCoins(
        @Query("vs_currency") currency: String = "inr",
        @Query("page") page: Int,

    ): CoinResponse

    @Headers(
        "accept: application/json",
        "x-cg-demo-api-key: $API_KEY"
    )
    @GET("coins/{id}/market_chart")
    suspend fun getCoinMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "inr",
        @Query("days") days: String
    ): CoinMarketChartResponse

}