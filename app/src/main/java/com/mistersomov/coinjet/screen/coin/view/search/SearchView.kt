package com.mistersomov.coinjet.screen.coin.view.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.screen.coin.view.PercentChanging
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.screen.coin.model.SearchViewState

@Composable
fun ViewSearchResult(
    viewState: SearchViewState.Display,
    onItemClicked: (Coin) -> Unit,
    onClearClicked: () -> Unit,
) {
    val recentSearchList = viewState.searchList
    val verticalPadding =
        if (recentSearchList.isEmpty()) (ButtonDefaults.IconSize / 2) else 0.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val stringId = when {
                recentSearchList.isNotEmpty() ->
                    R.string.crypto_search_headline_title_recent_searches
                else -> R.string.crypto_search_headline_title
            }
            Text(
                text = stringResource(id = stringId),
                color = CoinJetTheme.colors.surfaceVariant,
                style = CoinJetTheme.typography.titleMedium
            )
            if (recentSearchList.isNotEmpty()) {
                TextButton(
                    onClick = { onClearClicked.invoke() },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.crypto_search_clear_cache),
                        color = CoinJetTheme.colors.onPrimary
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(), color = CoinJetTheme.colors.surfaceVariant
        )
        if (recentSearchList.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.crypto_search_category_fullname),
                    color = CoinJetTheme.colors.surfaceVariant,
                    style = CoinJetTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.crypto_search_category_name),
                    color = CoinJetTheme.colors.surfaceVariant,
                    style = CoinJetTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.crypto_search_category_price),
                    color = CoinJetTheme.colors.surfaceVariant,
                    style = CoinJetTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(id = R.string.crypto_search_category_24h_changes),
                    color = CoinJetTheme.colors.surfaceVariant,
                    style = CoinJetTheme.typography.labelSmall
                )
            }
        } else {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = stringResource(id = R.string.crypto_search_headline_title_empty_list),
                color = CoinJetTheme.colors.surfaceVariant,
                style = CoinJetTheme.typography.titleSmall
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(items = recentSearchList, key = { coin -> coin.id }) { item ->
                CoinSearchDetails(
                    modifier = Modifier.padding(top = 10.dp),
                    coin = item,
                ) {
                    onItemClicked.invoke(item)
                }
            }
        }
    }
}

@Composable
fun CoinSearchDetails(
    modifier: Modifier,
    coin: Coin?,
    onItemClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClicked.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin?.imageUrl,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = coin?.fullName ?: "null",
                color = CoinJetTheme.colors.onPrimary,
                style = CoinJetTheme.typography.titleMedium,
            )
            Text(
                text = coin?.name ?: "null",
                color = CoinJetTheme.colors.onPrimary,
                style = CoinJetTheme.typography.titleSmall,
            )
            Text(
                text = coin?.price?.toString() ?: "null",
                color = CoinJetTheme.colors.onPrimary,
                style = CoinJetTheme.typography.titleMedium,
            )
            PercentChanging(percent = coin?.changepct24hour?.toDouble() ?: 0.0)
        }
    }
}