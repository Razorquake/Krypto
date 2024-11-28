package com.razorquake.krypto.presentation.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.presentation.coin_detail.CoinDetailScreen
import com.razorquake.krypto.presentation.coin_detail.CoinDetailViewModel
import com.razorquake.krypto.presentation.coin_list.CoinListScreen
import com.razorquake.krypto.presentation.coin_list.CoinListViewModel
import com.razorquake.krypto.presentation.coin_track.CoinTrackScreen
import com.razorquake.krypto.presentation.settings.SettingsScreen
import com.razorquake.krypto.ui.theme.Dimens.One
import com.razorquake.krypto.ui.theme.Dimens.Two

@Composable
fun Navigator(
){
    val navController = rememberNavController()
    val destination = listOf(
        Route.CoinList,
        Route.Settings
    )
    val backStackState = navController.currentBackStackEntryAsState().value
    val isBottomBarVisible = remember(key1 = backStackState){
        backStackState?.destination?.route in listOf(
            Route.CoinList.route,
            Route.CoinTrack.route,
            Route.Settings.route,
        )
    }
    Scaffold(
        bottomBar = {
            if(isBottomBarVisible)
            Box(
                modifier = Modifier.padding(One)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(Two),
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary,
                        clip = true
                    )
            ){
                NavigationBar(
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    destination.forEach { item->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = null
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navigateTo(navController, item.route)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if(isBottomBarVisible)
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    navigateTo(navController, Route.CoinTrack.route)
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(y = 68.dp)

                ) {
                Icon(painter = painterResource(id = Route.CoinTrack.icon),
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.CoinList.route,
            ){
                composable(
                    Route.CoinList.route
                ) {
                    val viewmodel: CoinListViewModel = hiltViewModel()
                    val state = viewmodel.state
                    CoinListScreen(
                        state = state.value,
                        event = viewmodel::onEvent,
                        bottomPadding = paddingValues.calculateBottomPadding(),
                        navigateToDetails = {coin->
                            navigateToDetails(navController, coin)
                        }
                    )

                }
                composable(
                    Route.CoinDetail.route
                ) {
                    val viewModel: CoinDetailViewModel = hiltViewModel()
                    val state by viewModel.state
                    val chartState by viewModel.chartState.collectAsState()
                    navController.previousBackStackEntry?.savedStateHandle?.get<Coin>("coin")
                        ?.let { coin->
                            CoinDetailScreen(
                                coin = coin,
                                state = state,
                                onEvent = viewModel::onEvent,
                                navigateUp = {navController.navigateUp()},
                                chartState = chartState
                            )
                        }
                }
                composable(
                    Route.CoinTrack.route
                ) {
                    CoinTrackScreen(
                        bottomPadding = paddingValues.calculateBottomPadding()
                    )
                }
                composable(
                    Route.Settings.route
                ) {
                    SettingsScreen(
                        bottomPadding = paddingValues.calculateBottomPadding()
                    )
                }
            }
        }
    }
}

private fun navigateToDetails(navController: NavController, coin: Coin){
    navController.currentBackStackEntry?.savedStateHandle?.set("coin", coin)
    navController.navigate(Route.CoinDetail.route)
}
private fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreenRoute ->
            popUpTo(homeScreenRoute) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}