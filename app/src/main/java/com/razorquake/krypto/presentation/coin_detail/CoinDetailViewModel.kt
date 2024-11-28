package com.razorquake.krypto.presentation.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.domain.usecases.CoinUseCases
import com.razorquake.krypto.presentation.chart.ChartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val coinUseCases: CoinUseCases,

): ViewModel() {

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    private val _chartState = MutableStateFlow<ChartState>(ChartState.Loading)
    val chartState: StateFlow<ChartState> = _chartState

    private val days = immutableListOf("1", "7", "30", "90", "180", "365")
    private fun fetchCoinChart(coinId: String, day: Int) {
        viewModelScope.launch {
            _chartState.value = ChartState.Loading
            try {
                _state.value = _state.value.copy(index = day)
                val chartData = coinUseCases.getChart(coinId, days[day])
                _chartState.value = ChartState.Success(
                    chartData,
                    chartData.first().price,
                    chartData.last().price,
                    (chartData.last().price - chartData.first().price) / chartData.first().price * 100,
                    chartData.first().date,
                    chartData.last().date
                )
            } catch (e: Exception) {
                _chartState.value = ChartState.Error("Failed to fetch chart data: ${e.message}")
            }
        }
    }

    private fun checkBookmarkStatus(id: String) {
        viewModelScope.launch {
            val savedArticle = coinUseCases.getCoin(id)
            _state.value = _state.value.copy(isBookmarked = savedArticle != null)
        }
    }

    private suspend fun upsertCoin(coin: Coin) {
            coinUseCases.upsertCoin(coin)
            _state.value = _state.value.copy(isBookmarked = true)
    }

    private suspend fun deleteCoin(id: String) {
            coinUseCases.deleteCoin(id)
        _state.value = _state.value.copy(isBookmarked = false)
    }

    fun onEvent(event: CoinDetailEvent){
        when(event){
            is CoinDetailEvent.CheckBookmark -> {
                checkBookmarkStatus(event.id)
            }
            is CoinDetailEvent.UpsertDeleteCoin -> {
                viewModelScope.launch {
                    val coin = coinUseCases.getCoin(event.coin.id)
                    if (coin == null) {
                        upsertCoin(event.coin)
                    } else {
                        deleteCoin(event.coin.id)
                    }
                }
            }

            is CoinDetailEvent.SetIndex -> {

                fetchCoinChart(event.id, event.index)
            }
        }
    }
}