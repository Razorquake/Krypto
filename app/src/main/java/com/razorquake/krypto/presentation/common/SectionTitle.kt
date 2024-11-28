package com.razorquake.krypto.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.razorquake.krypto.R

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
){
    Text(
        text = title,
        modifier = modifier.semantics { heading() },
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.text_title),
        style = MaterialTheme.typography.titleLarge
    )
}