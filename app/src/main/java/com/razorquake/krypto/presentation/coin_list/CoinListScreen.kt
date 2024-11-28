package com.razorquake.krypto.presentation.coin_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.razorquake.krypto.data.remote.dto.Coin

@Composable
fun CoinListScreen(
    bottomPadding: Dp = 0.dp,
    state: CoinListState,
    event: (CoinListEvent) -> Unit,
    navigateToDetails: (Coin) -> Unit
) {
    val coins = state.coins!!.collectAsLazyPagingItems()
    CardList(
        coins = coins,
        onRefresh = { event(CoinListEvent.LoadCoins) },
        onClick = { navigateToDetails(it)},
        modifier = Modifier.fillMaxSize().statusBarsPadding()
    )
}