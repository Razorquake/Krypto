package com.razorquake.krypto.presentation.nav

import androidx.annotation.DrawableRes
import com.razorquake.krypto.R



sealed class Route(
    val route: String,
    @DrawableRes val icon: Int
) {
    data object CoinList : Route("coin_list", R.drawable.baseline_list_24)
    data object CoinTrack : Route("coin_track", R.drawable.baseline_trending_up_24)
    data object Settings : Route("settings", R.drawable.baseline_settings_24)
    data object CoinDetail : Route("coin_detail", R.drawable.baseline_trending_up_24)
}