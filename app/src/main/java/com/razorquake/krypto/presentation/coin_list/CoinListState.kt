package com.razorquake.krypto.presentation.coin_list

import androidx.paging.PagingData
import com.razorquake.krypto.data.remote.dto.Coin
import kotlinx.coroutines.flow.Flow

data class CoinListState(
    val coins: Flow<PagingData<Coin>>? = null
)
