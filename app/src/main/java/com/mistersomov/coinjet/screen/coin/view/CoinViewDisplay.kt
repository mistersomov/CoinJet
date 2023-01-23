package com.mistersomov.coinjet.screen.coin.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.component.ListItem
import com.mistersomov.coinjet.core_ui.effect.animateDigitColor
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.screen.coin.model.CoinViewState
import com.mistersomov.coinjet.utils.asPercentage
import com.mistersomov.coinjet.utils.formatCurrencyToDisplay

@Composable
fun CoinViewDisplay(
    viewState: CoinViewState,
    navController: NavController,
    onCoinClicked: (String) -> Unit,
) {
    val coinList = remember { viewState.coinList }
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxHeight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = true,
    ) {
        items(items = coinList, key = { coin -> coin.symbol }) { coin ->
            ListItem(modifier = Modifier.padding(horizontal = 6.dp),
                content = {
                    CoinDetails(coin = coin)
                }) {
                onCoinClicked.invoke(coin.id)
            }
        }
    }
}

@Composable
fun CoinDetails(coin: Coin) {
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
                modifier = Modifier.fillMaxWidth(0.4f),
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
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    drawRoundRect(
                        color = backgroundColor,
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }
            },
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