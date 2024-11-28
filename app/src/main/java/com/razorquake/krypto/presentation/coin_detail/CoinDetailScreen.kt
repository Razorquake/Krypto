package com.razorquake.krypto.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.razorquake.krypto.R
import com.razorquake.krypto.data.remote.dto.Coin
import com.razorquake.krypto.data.remote.formatters.getLocalizedDateTime
import com.razorquake.krypto.presentation.chart.ChartState
import com.razorquake.krypto.presentation.chart.CoinChartViewModel
import com.razorquake.krypto.presentation.chart.LineChart
import com.razorquake.krypto.presentation.coin_detail.components.Header
import com.razorquake.krypto.presentation.coin_detail.components.LoadingItem
import com.razorquake.krypto.presentation.coin_detail.components.SectionInfoItem
import com.razorquake.krypto.presentation.common.SectionTitle
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    coin: Coin,
    state: CoinDetailState,
    onEvent: (CoinDetailEvent) -> Unit,
    navigateUp: () -> Unit,
    chartState: ChartState
){
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(
        key1 = coin
    ) {
        onEvent(CoinDetailEvent.CheckBookmark(coin.id))
    }
    LaunchedEffect(
        key1 = coin
    ) {
        onEvent(CoinDetailEvent.SetIndex(coin.id, state.index))
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(CoinDetailEvent.UpsertDeleteCoin(coin))
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if(state.isBookmarked)
                                    R.drawable.baseline_star_24
                                else
                                    R.drawable.baseline_star_outline_24
                            ) ,
                            contentDescription = null,
                            tint = colorResource(R.color.text_title)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item{
                Header(imageUrl = coin.image, name = coin.name, symbol = coin.symbol)
                Spacer(modifier = Modifier.size(16.dp))
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).alpha(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Medium,
                    text = "₹${coin.currentPrice}",
                    maxLines = 1
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(190.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    when(val graphState =chartState){
                        is ChartState.Success ->{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {

                                    Text(
                                        text = "Price ${graphState.startPriceDate.format(
                                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colorResource(R.color.text_title),
                                        textAlign = TextAlign.Start,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "₹${graphState.startPrice}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Start,
                                        color = colorResource(R.color.text_title),
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1
                                    )
                                }
                                Card(
                                    modifier = Modifier
                                        .sizeIn(minWidth = 72.dp),
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(
                                        contentColor = if(graphState.pricePercentageChange>=0) Color.Green else Color.Red
                                    )
                                ) {
                                    Text(
                                        text = "${graphState.pricePercentageChange.toString().take(5)}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 1.dp)
                                            .align(Alignment.End),
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.End,
                                        maxLines = 1
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            AnimatedVisibility(
                                visible = true,
                                exit = ExitTransition.None
                            ) {
                                LineChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    prices = graphState.data,
                                    graphColor = if (graphState.pricePercentageChange>=0) Color.Green else Color.Red,
                                    showDashedLine = true,
                                    showYLabels = true
                                )
                            }
                        }
                        is ChartState.Loading -> {
                            LoadingItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                text = "Loading chart data..."
                            )
                        }
                        is ChartState.Error -> {
                            var startAnimation by remember {
                                mutableStateOf(false)
                            }

                            val alphaAnimation by animateFloatAsState(
                                targetValue = if (startAnimation) 0.3f else 0f,
                                animationSpec = tween(durationMillis = 1000),
                                label = ""
                            )

                            LaunchedEffect(key1 = true) {
                                startAnimation = true
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_trending_up_24),
                                    contentDescription = null,
                                    tint = if (isSystemInDarkTheme()) LightGray else DarkGray,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .alpha(alphaAnimation)
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .alpha(alphaAnimation),
                                    text = "Error loading chart data",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSystemInDarkTheme()) LightGray else DarkGray,
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                val options = listOf("24H", "1W", "1M", "3M", "6M", "1Y")
                val borderColor = colorResource(R.color.input_background)
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.heightIn(min = 48.dp)
                        .padding(horizontal = 8.dp)
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                ) {
                    options.forEachIndexed { index, s ->
                        val isSelected = index == state.index
                        val isFirst = index == 0
                        val isLast = index == options.size - 1
                        SegmentedButton(
                            onClick = {
                                onEvent(CoinDetailEvent.SetIndex(coin.id, index))
                            },
                            selected = isSelected,
                            shape =  when {
                                isSelected -> RoundedCornerShape(16.dp)
                                isFirst -> RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                isLast -> RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                                else -> RoundedCornerShape(0.dp)
                            },
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = borderColor,
                                activeContentColor = colorResource(R.color.text_title),
                                inactiveContentColor = colorResource(R.color.text_title)
                            ),
                            border = SegmentedButtonDefaults.borderStroke(Color.Transparent),
                            modifier = Modifier
                                .weight(if (isSelected) 1.05f else 1f)
                                .zIndex(if (isSelected) 1f else 0f)
                                .clip(
                                    when {
                                        isSelected -> RoundedCornerShape(16.dp)
                                        isFirst -> RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                        isLast -> RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                                        else -> RoundedCornerShape(0.dp)
                                    }
                                )
                                .height(48.dp)
                                .then(
                                if (!isSelected) {
                                    Modifier.drawWithContent {
                                        drawContent()
                                        if (!isFirst && state.index!=(index - 1)) {
                                            // Draw left border
                                            drawLine(
                                                color = borderColor,
                                                start = Offset(0f, 0.1f),
                                                end = Offset(0f, size.height-0.1f),
                                                strokeWidth = 1.dp.toPx()
                                            )
                                        }
                                        if (!isLast && state.index!=(index + 1)) {
                                            // Draw right border
                                            drawLine(
                                                color = borderColor,
                                                start = Offset(size.width, 0.1f),
                                                end = Offset(size.width, size.height-0.1f),
                                                strokeWidth = 1.dp.toPx()
                                            )
                                        }
                                    }
                                } else Modifier
                            )
                            ,
                            icon = {}
                        ) {
                            Text(s)
                        }
                    }

                }
            }
            item {
                Spacer(modifier = Modifier.size(16.dp))
                SectionTitle(
                    title = "Market Data",
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            color = colorResource(R.color.input_background),
                            shape = MaterialTheme.shapes.large
                        )
                ) {
                    SectionInfoItem(
                        name = "Market Cap",
                        value = "₹${coin.marketCap}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Market Cap Rank",
                        value = coin.marketCapRank.toString(),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Fully Diluted Valuation",
                        value = "₹${coin.fullyDilutedValuation}",
                        showDivider = true
                    )

                    SectionInfoItem(
                        name = "Trading Volume",
                        value = "₹${coin.totalVolume}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "High 24h",
                        value = "₹${coin.high24h}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Low 24h",
                        value = "₹${coin.low24h}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Available Supply",
                        value = coin.circulatingSupply.toString(),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Max Supply",
                        value = coin.maxSupply.toString(),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Rank",
                        value = coin.marketCapRank.toString(),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Total Supply",
                        value = coin.totalSupply.toString(),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "All-Time High Price",
                        value = "₹${coin.ath}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "All-Time High Date",
                        value = getLocalizedDateTime(coin.athDate),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "All-Time Low Price",
                        value = "₹${coin.atl}",
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "All-Time Low Date",
                        value = getLocalizedDateTime(coin.atlDate),
                        showDivider = true
                    )
                    SectionInfoItem(
                        name = "Last Updated",
                        value = getLocalizedDateTime(coin.athDate),
                        showDivider = false
                    )

                }
            }
        }
    }
}