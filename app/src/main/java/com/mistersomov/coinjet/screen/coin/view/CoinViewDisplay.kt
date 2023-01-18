package com.mistersomov.coinjet.screen.coin.view

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mistersomov.coinjet.core_ui.component.ListItem
import com.mistersomov.coinjet.screen.coin.ListItemDetails
import com.mistersomov.coinjet.screen.coin.model.CoinListViewState

@Composable
fun CoinViewDisplay(
    viewState: CoinListViewState.Display,
    navController: NavController,
    onCoinClicked: (String) -> Unit,
) {
    val coinList = viewState.coinList
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
                    ListItemDetails(coin = coin)
                }) {
                onCoinClicked.invoke(coin.id)
            }
        }
    }
}