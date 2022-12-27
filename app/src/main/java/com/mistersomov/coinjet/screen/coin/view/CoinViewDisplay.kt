package com.mistersomov.coinjet.screen.coin.view

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.core_ui.component.ListItem
import com.mistersomov.coinjet.screen.coin.model.CoinViewState
import com.mistersomov.coinjet.utils.asPercentage

@Composable
fun CoinViewDisplay(
    viewState: CoinViewState.Display,
    navController: NavController,
) {
    val coinList = viewState.coinList
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxHeight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = true,
    ) {
        items(items = coinList, key = { coin -> coin.id }) { coin ->
            ListItem(modifier = Modifier.padding(horizontal = 6.dp),
                content = {
                    CoinDetails(coin = coin)
                }) {
            }
        }
    }
}

@Composable
fun CoinDetails(coin: Coin, isFavorite: Boolean = false) {
    val imageModel by remember { mutableStateOf(coin.imageUrl) }
    val name by remember { mutableStateOf(coin.name) }
    val fullName by remember { mutableStateOf(coin.fullName) }

    AsyncImage(
        model = imageModel,
        modifier = Modifier
            .padding(start = if (isFavorite) 10.dp else 0.dp)
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
        Column(
            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            val lastUpdate = String.format(
                stringResource(id = R.string.crypto_last_update),
                coin.lastUpdate
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = coin.price,
                    color = animatePriceColor(price = coin.price.toDouble()),
                    style = CoinJetTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
                PercentChanging(
                    modifier = Modifier.padding(start = 6.dp),
                    percent = coin.changepct24hour.toDouble(),
                )
            }
            Text(
                text = lastUpdate,
                color = CoinJetTheme.colors.onSurfaceVariant,
                style = CoinJetTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                maxLines = 1
            )
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
        CoinJetTheme.colors.surfaceVariant
    } else if (percent > 0) {
        CoinJetTheme.colors.green
    } else CoinJetTheme.colors.errorContainer

    val textColor = if (percentValue == 0.0) {
        CoinJetTheme.colors.onSurfaceVariant
    } else if (percentValue > 0) {
        CoinJetTheme.colors.onGreen
    } else CoinJetTheme.colors.error

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
            color = textColor,
            style = CoinJetTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun animatePriceColor(price: Double): Color {
    val previousPrice by remember { mutableStateOf(price) }
    val initialColor = CoinJetTheme.colors.onSurface
    val targetColor = if (price == previousPrice) {
        initialColor
    } else if (price > previousPrice) {
        CoinJetTheme.colors.onGreen
    } else {
        CoinJetTheme.colors.error
    }
    val priceColor = remember { Animatable(initialColor) }
    val duration = 2000

    LaunchedEffect(key1 = price, block = {
        priceColor.animateTo(targetColor, tween(durationMillis = duration))
        priceColor.animateTo(initialColor, tween(durationMillis = duration))
    })

    return priceColor.value
}