package com.razorquake.krypto.presentation.coin_list

sealed class CoinListEvent {
    data object LoadCoins: CoinListEvent()

}
