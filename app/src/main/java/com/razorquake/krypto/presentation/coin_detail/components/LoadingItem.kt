package com.razorquake.krypto.presentation.coin_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.razorquake.krypto.R

@Composable
fun LoadingItem(
    modifier: Modifier,
    text: String = "Loading data..."
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        CircularProgressIndicator(color = colorResource(R.color.text_title))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(R.color.text_title)
        )
    }
}