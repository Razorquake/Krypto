package com.razorquake.krypto.data.remote.mappers

import com.razorquake.krypto.data.remote.dto.CoinMarketChartResponse
import com.razorquake.krypto.data.remote.dto.MarketChartDataPoint
import java.time.Instant
import java.time.ZoneId

fun mapMarketChartDataPointList(marketChartDto: CoinMarketChartResponse): List<MarketChartDataPoint> {
    val divAmount = when(marketChartDto.prices.size) {
        in 0..200 -> 1
        in 200..400 -> 2
        in 400..800 -> 6
        in 800..1600 -> 10
        else -> 12
    }

    return marketChartDto.prices.filterIndexed { index, _ ->
        index % divAmount == 0
    }.map {
        mapMarketChartDataPoint(it)
    }
}

private fun mapMarketChartDataPoint(dataPointList: List<Double>): MarketChartDataPoint {
    val date =
        Instant.ofEpochMilli(dataPointList.first().toLong()).atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    val price = dataPointList[1]

    return MarketChartDataPoint(
        date = date,
        price = price
    )
}