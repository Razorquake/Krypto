package com.razorquake.krypto.presentation.coin_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.presentation.common.CoinCard
import com.razorquake.krypto.presentation.common.CoinCardShimmer
import com.razorquake.krypto.presentation.common.EmptyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardList(
    modifier: Modifier = Modifier,
    coins: LazyPagingItems<Coin>,
    onRefresh: () -> Unit,
    onClick: (Coin) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        onRefresh = onRefresh,
        state = pullToRefreshState,
        isRefreshing = coins.loadState.refresh is LoadState.Loading,
        modifier = modifier.fillMaxSize()
    ) {
        val handlePagingResult = handlePagingResult(coins = coins)
        if (handlePagingResult) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(all = 6.dp),
                state = lazyListState
            ) {
                items(coins.itemCount) { index ->
                    coins[index]?.let { coin ->
                        CoinCard(
                            item = coin,
                            onCoinItemClick = { onClick(coin) },
                        )
                    }
                }
            }
        }

    }
}
@Composable
fun handlePagingResult(
    coins: LazyPagingItems<Coin>,
): Boolean {
    val loadState = coins.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> {
            loadState.refresh as LoadState.Error
        }

        loadState.append is LoadState.Error -> {
            loadState.append as LoadState.Error
        }

        loadState.prepend is LoadState.Error -> {
            loadState.prepend as LoadState.Error
        }

        else -> null
    }
    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false
        }

        error != null -> {
            EmptyScreen(error)
            false
        }

        coins.itemCount == 0 -> {
            EmptyScreen()
            false
        }

        else -> {
            true
        }

    }
}

@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        repeat(10) {
            CoinCardShimmer(modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}
