package com.mistersomov.coinjet.screen.coin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.effect.animateDigitColor
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.screen.coin.PercentChanging
import com.mistersomov.coinjet.utils.formatCurrencyToDisplay

@Composable
fun ListItemDetails(coin: Coin) {
    val imageModel by remember { mutableStateOf(coin.imageUrl) }
    val name by remember { mutableStateOf(coin.symbol) }
    val fullName by remember { mutableStateOf(coin.name) }

    AsyncImage(
        model = imageModel,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop
    )
    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row {
                Text(
                    text = name,
                    color = CoinJetTheme.colors.onSurface,
                    style = CoinJetTheme.typography.titleMedium,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .align(Alignment.Bottom),
                    text = "/" + coin.toSymbol,
                    color = CoinJetTheme.colors.onSurfaceVariant,
                    style = CoinJetTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            Text(
                text = fullName,
                color = CoinJetTheme.colors.onSurfaceVariant,
                style = CoinJetTheme.typography.bodySmall,
                maxLines = 1
            )
        }
        Row(
            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.coin_price_label),
                    color = CoinJetTheme.colors.onSurface,
                    style = CoinJetTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Text(
                    text = coin.priceUsd.formatCurrencyToDisplay(),
                    color = animateDigitColor(
                        digit = coin.priceUsd,
                        initialColor = CoinJetTheme.colors.onSurface
                    ),
                    style = CoinJetTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.coin_24h_changes_label),
                    color = CoinJetTheme.colors.onSurface,
                    style = CoinJetTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                PercentChanging(
                    modifier = Modifier.padding(start = 6.dp),
                    percent = coin.changePercent24Hr,
                )
            }
        }
    }
}