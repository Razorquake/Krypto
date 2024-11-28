package com.razorquake.krypto.presentation.coin_detail

import com.razorquake.krypto.data.remote.dto.Coin

sealed class CoinDetailEvent {
    data class UpsertDeleteCoin(val coin: Coin): CoinDetailEvent()
    data class CheckBookmark(val id: String): CoinDetailEvent()
    data class SetIndex(val id: String, val index: Int): CoinDetailEvent()
}