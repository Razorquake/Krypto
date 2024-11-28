package com.razorquake.krypto.data.remote.dto


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CoinMarketChartResponse(
    @SerializedName("market_caps")
    val marketCaps: List<List<Double>>,
    @SerializedName("prices")
    val prices: List<List<Double>>,
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double>>
)
data class MarketChartDataPoint(
    val date: LocalDateTime,
    val price: Double
)