package com.razorquake.krypto.presentation.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorquake.krypto.data.remote.mappers.mapMarketChartDataPointList
import com.razorquake.krypto.domain.repo.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

@HiltViewModel
class CoinChartViewModel @Inject constructor(private val repository: CoinRepository): ViewModel() {
    private val _chartState = MutableStateFlow<ChartState>(ChartState.Loading)
    val chartState: StateFlow<ChartState> = _chartState

    private val days = immutableListOf("1", "7", "30", "90", "180", "365")
    fun fetchCoinChart(coinId: String, day: Int) {
        viewModelScope.launch {
            _chartState.value = ChartState.Loading
            try {
                val response = repository.getCoinMarketChart(coinId, days[day])
                val chartData = mapMarketChartDataPointList(response)
                _chartState.value = ChartState.Success(
                    chartData,
                    chartData.first().price,
                    chartData.last().price,
                    (chartData.last().price - chartData.first().price) / chartData.first().price * 100,
                    chartData.first().date,
                    chartData.last().date)
            } catch (e: Exception) {
                _chartState.value = ChartState.Error("Failed to fetch chart data: ${e.message}")
            }
        }
    }
}