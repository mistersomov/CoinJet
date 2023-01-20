package com.mistersomov.coinjet.screen.coin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.utils.asPercentage

@Composable
fun PercentChanging(
    modifier: Modifier = Modifier,
    percent: Double,
) {
    val percentValue by remember { mutableStateOf(percent) }

    val backgroundColor = if (percentValue == 0.0) {
        CoinJetTheme.colors.onSurfaceVariant
    } else if (percent > 0) {
        CoinJetTheme.colors.green
    } else CoinJetTheme.colors.red

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
            .size(width = 60.dp, height = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            text = "${percent.asPercentage()}%",
            color = CoinJetTheme.colors.surface,
            style = CoinJetTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}