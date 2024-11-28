package com.razorquake.krypto.presentation.coin_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.razorquake.krypto.R

@Composable
fun SectionInfoItem(
    name: String,
    value: String,
    showDivider: Boolean
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = name,
            color = colorResource(R.color.placeholder),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.text_title),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    if (
        showDivider
    )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp).alpha(.2f),
            color = colorResource(R.color.placeholder)
        )
}