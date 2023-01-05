package com.mistersomov.coinjet.screen.coin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.component.Search
import com.mistersomov.coinjet.screen.coin.model.*
import com.mistersomov.coinjet.screen.coin.view.CoinViewDisplay
import com.mistersomov.coinjet.screen.coin.view.CoinViewLoading
import com.mistersomov.coinjet.screen.coin.view.CoinViewSimpleDetails
import com.mistersomov.coinjet.screen.coin.view.search.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoinScreen(navController: NavController, viewModel: CoinViewModel = hiltViewModel()) {
    val viewState = viewModel.coinViewState.collectAsState()
    val searchViewState = viewModel.searchViewState.collectAsState()
    val detailsViewState = viewModel.coinDetailsViewState.collectAsState()

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    LaunchedEffect(key1 = scaffoldState, block = {
        scaffoldState.reveal()
    })

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = { },
        frontLayerScrimColor = Color.Unspecified,
        backLayerBackgroundColor = CoinJetTheme.colors.primary,
        peekHeight = 80.dp,
        backLayerContent = {
            Search(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                placeholderText = stringResource(id = R.string.crypto_search_placeholder),
                onFocusChanged = {
                    with(viewModel) {
                        cancelSimpleDetailsJob()
                        obtainSearchEvent(SearchEvent.Click)
                    }
                    scope.launch { scaffoldState.reveal() }
                },
                onValueChanged = { viewModel.obtainSearchEvent(SearchEvent.LaunchSearch(it)) },
                onCancelClicked = { viewModel.hideSearch() },
                onRemoveQuery = { viewModel.obtainSearchEvent(SearchEvent.Click) },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (val currentSearchState = searchViewState.value) {
                        is SearchViewState.Hide -> Unit
                        is SearchViewState.NoItems -> SearchViewNoItems()
                        is SearchViewState.FirstSearch -> SearchViewFirst()
                        is SearchViewState.Recent -> SearchViewRecent(
                            viewState = currentSearchState,
                            onItemClicked = {
                                scope.launch { scaffoldState.reveal() }
                                viewModel.obtainEvent(CoinEvent.Click(it.symbol))
                            },
                            onClearClicked = { viewModel.obtainSearchEvent(SearchEvent.ClearCache) }
                        )
                        is SearchViewState.Global -> SearchViewGlobal(
                            viewState = currentSearchState,
                            onItemClicked = {
                                with(viewModel) {
                                    obtainEvent(CoinEvent.Click(it.symbol))
                                    obtainSearchEvent(SearchEvent.Save(it))
                                }
                                scope.launch { scaffoldState.reveal() }
                            })
                    }
                }
            }
            when (val current = detailsViewState.value) {
                is CoinDetailsViewState.SimpleDetails -> CoinViewSimpleDetails(
                    modifier = Modifier.padding(
                        horizontal = 8.dp
                    ),
                    coin = current.coin,
                    onCancelClicked = {
                        with(viewModel) {
                            hideSearch()
                            cancelSimpleDetailsJob()
                        }
                    }
                )
                else -> Unit
            }
        },
        frontLayerElevation = 20.dp,
        frontLayerBackgroundColor = CoinJetTheme.colors.surface,
        frontLayerContent = {
            when (val currentState = viewState.value) {
                is CoinViewState.Loading -> CoinViewLoading()
                is CoinViewState.Display ->
                    AnimatedVisibility(visible = true) {
                        CoinViewDisplay(
                            navController = navController,
                            viewState = currentState,
                            onCoinClicked = {
                                scope.launch { scaffoldState.reveal() }
                                with(viewModel) {
                                    obtainEvent(CoinEvent.Click(it))
                                    hideSearch()
                                }
                            }
                        )
                    }
                is CoinViewState.NoItems -> Unit
                //is CryptoViewState.Error -> CryptoViewError()
                else -> throw NotImplementedError(
                    stringResource(id = R.string.crypto_implementation_state_error)
                )
            }
        })

    LaunchedEffect(Unit, block = {
        viewModel.obtainEvent(CoinEvent.FetchData)
    })
}