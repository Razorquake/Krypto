package com.razorquake.krypto.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.razorquake.krypto.domain.usecases.CoinUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinUseCases: CoinUseCases
): ViewModel() {
    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    private fun getCoins(){
        viewModelScope.launch {
            val coins = coinUseCases.getTopCoins().cachedIn(viewModelScope)
            _state.value = _state.value.copy(coins = coins)
        }
    }
    init {
        getCoins()
    }
    fun onEvent(event: CoinListEvent){
        when(event){
            is CoinListEvent.LoadCoins -> {
                getCoins()
            }
            else ->{}
        }
    }
}