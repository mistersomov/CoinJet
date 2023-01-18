package com.mistersomov.coinjet.screen.coin.view.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.component.ListItem
import com.mistersomov.coinjet.screen.coin.ListItemDetails
import com.mistersomov.coinjet.screen.coin.model.favorite.FavoriteViewState

@Composable
fun FavoriteDisplay(
    modifier: Modifier = Modifier,
    viewState: FavoriteViewState.Display,
    onCoinClicked: (String) -> Unit
) {
    val favoriteList = viewState.favoriteCoinList
    val listState = rememberLazyListState()

    Card(
        modifier = modifier,
        backgroundColor = CoinJetTheme.colors.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                    text = stringResource(id = R.string.coin_favorites_label),
                    style = CoinJetTheme.typography.labelLarge,
                    color = CoinJetTheme.colors.onSurface
                )
            }
            item {
                Divider(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    color = CoinJetTheme.colors.outline
                )
            }
            items(items = favoriteList, key = { coin -> coin.symbol }) { coin ->
                ListItem(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    isFavorite = remember {
                        mutableStateOf(true)
                    },
                    content = {
                        ListItemDetails(coin = coin)
                    }) {
                    onCoinClicked.invoke(coin.id)
                }
            }
        }
    }
}