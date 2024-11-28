package com.razorquake.krypto.presentation.chart

import com.razorquake.krypto.data.remote.dto.MarketChartDataPoint
import java.time.LocalDateTime

sealed class ChartState {
    data object Loading : ChartState()
    data class Success(val data: List<MarketChartDataPoint>, val startPrice: Double, val endPrice: Double, val pricePercentageChange: Double, val startPriceDate: LocalDateTime, val endPriceDate: LocalDateTime) : ChartState()
    data class Error(val message: String) : ChartState()
}