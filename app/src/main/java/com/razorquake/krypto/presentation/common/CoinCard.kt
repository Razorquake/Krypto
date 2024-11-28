package com.razorquake.krypto.presentation.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.razorquake.krypto.R
import com.razorquake.krypto.data.remote.dto.Coin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoinCard(
    modifier: Modifier = Modifier,
    item: Coin,
    onCoinItemClick: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val setupSharedElement = rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .semantics(mergeDescendants = true) {},
        onClick = {
            if (setupSharedElement.value) {
                onCoinItemClick()
            } else {
                setupSharedElement.value = true

                coroutineScope.launch {
                    delay(50L)
                    onCoinItemClick()
                }
            }
        },
        colors = CardDefaults.cardColors(
            contentColor = colorResource(R.color.text_title)
        ),
        shape = MaterialTheme.shapes.large
    ) {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                modifier = Modifier.size(30.dp).clip(MaterialTheme.shapes.medium),
                model = item.image,
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(
                            text = item.marketCapRank.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 1.dp, bottom = 1.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = item.symbol,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.CenterEnd
            ) {

//                if (showChart.value) {
//                    androidx.compose.animation.AnimatedVisibility(visible = animateChart.value) {
//
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            LineChart(
//                                modifier = Modifier.size(width = 48.dp, height = 29.dp),
//                                data = item().sparklineData,
//                                graphColor = item().trendColor,
//                                showDashedLine = true
//                            )
//
//                            // Invisible text with max price size to determine the max possible size of this column
//                            Text(
//                                text = "$100,000.00",
//                                modifier = Modifier.alpha(0f)
//                            )
//                        }
//
//                    }
//                }

                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        modifier = Modifier
                            .width(IntrinsicSize.Max),
                        text = "₹${item.currentPrice.toString()}",
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(1.dp))

                    Card(
                        modifier = Modifier
                            .requiredWidth(72.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(
                            containerColor = if(item.priceChangePercentage24h>0) Color.Green else Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = item.priceChangePercentage24h.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 1.dp)
                                .align(Alignment.End),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }

                }

            }

        }

    }

}
fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition(label = "")
    val alpha = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                delayMillis = 1000
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    ).value
    background(color = colorResource(id = R.color.shimmer).copy(alpha))
}
@Composable
fun CoinCardShimmer(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier.size(30.dp).clip(MaterialTheme.shapes.medium).shimmerEffect()
            )
            // Left column: Coin name and symbol
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Market cap rank shimmer
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Symbol shimmer
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(16.dp)
                            .shimmerEffect()
                    )
                }
            }

            // Right column: Price and change percentage
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Price shimmer
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Change percentage shimmer
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(24.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}