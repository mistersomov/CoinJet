package com.mistersomov.coinjet.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.component.Search
import com.mistersomov.coinjet.navigation.NavigationTree
import com.mistersomov.coinjet.screen.search.view.SearchViewFirst
import com.mistersomov.coinjet.screen.search.view.SearchViewGlobal
import com.mistersomov.coinjet.screen.search.view.SearchViewRecent
import com.mistersomov.coinjet.screen.search.model.SearchEvent

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    val viewState by viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(initial = null)

    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CoinJetTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Search(
                placeholderText = stringResource(id = R.string.crypto_search_placeholder),
                isAvailable = true,
                onFocusChanged = { viewModel.obtainEvent(SearchEvent.ShowRecentSearch) },
                onValueChanged = { viewModel.obtainEvent(SearchEvent.StartSearching(it)) },
                onCancelClicked = {
                    navController.navigate(NavigationTree.Root.Main.name) {
                        popUpTo(NavigationTree.Root.Search.name)
                    }
                }) {
            }
            when {
                viewState.recentSearchList.isNotEmpty() && viewState.globalSearchList.isEmpty() -> {
                    SearchViewRecent(
                        viewState = viewState,
                        onItemClicked = {},
                        onClearClicked = {
                            viewModel.obtainEvent(SearchEvent.ClearCache)
                        })
                }
                viewState.recentSearchList.isNotEmpty() && viewState.globalSearchList.isNotEmpty() -> SearchViewGlobal(
                    viewState = viewState,
                    onItemClicked = { viewModel.obtainEvent(SearchEvent.Click(it)) }
                )
                viewState.recentSearchList.isEmpty() && viewState.globalSearchList.isNotEmpty() -> SearchViewGlobal(
                    viewState = viewState,
                    onItemClicked = { viewModel.obtainEvent(SearchEvent.Click(it)) }
                )
                else -> {

                }
            }
        }
    }
}