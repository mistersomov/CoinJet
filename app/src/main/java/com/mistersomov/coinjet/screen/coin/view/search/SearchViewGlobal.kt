package com.mistersomov.coinjet.screen.coin.view.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.screen.coin.model.SearchViewState

@Composable
fun SearchViewGlobal(
    viewState: SearchViewState.Global,
    onItemClicked: (Coin) -> Unit
) {
    val searchList = viewState.globalSearchList

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.crypto_search_headline_title),
            color = CoinJetTheme.colors.surfaceVariant,
            style = CoinJetTheme.typography.titleMedium
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            color = CoinJetTheme.colors.surfaceVariant
        )
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
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            items(items = searchList, key = { coin -> coin.id }) { item ->
                SearchCoinDetails(
                    coin = item,
                ) {
                    onItemClicked.invoke(item)
                }
            }
        }
    }
}