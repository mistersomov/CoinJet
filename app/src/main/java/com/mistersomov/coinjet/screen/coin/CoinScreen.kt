package com.mistersomov.coinjet.screen.coin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.component.Search
import com.mistersomov.coinjet.navigation.NavigationTree
import com.mistersomov.coinjet.screen.coin.model.*
import com.mistersomov.coinjet.screen.coin.view.CoinViewDisplay
import com.mistersomov.coinjet.screen.coin.view.CoinViewLoading
import com.mistersomov.coinjet.screen.coin.view.CoinViewSimpleDetails
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoinScreen(navController: NavController, viewModel: CoinViewModel = hiltViewModel()) {
    val viewState by viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(initial = null)

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = { },
        frontLayerScrimColor = Color.Unspecified,
        backLayerBackgroundColor = CoinJetTheme.colors.primary,
        peekHeight = 90.dp,
        backLayerContent = {
            Search(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholderText = stringResource(id = R.string.crypto_search_placeholder),
                isAvailable = true,
                onFocusChanged = {
                    viewModel.obtainEvent(CoinEvent.SearchClick)
                },
                onValueChanged = {},
                onCancelClicked = {},
                onRemoveQuery = {},
            )
            viewState.details?.let { coin ->
                CoinViewSimpleDetails(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    coin = coin,
                    onCancelClicked = remember {
                        {
                            viewModel.obtainEvent(CoinEvent.SimpleDetailsClose)
                        }
                    }
                )
            }
        },
        frontLayerElevation = 20.dp,
        frontLayerBackgroundColor = CoinJetTheme.colors.surface,
        frontLayerContent = {
            when {
                viewState.isLoading && viewState.coinList.isEmpty() -> CoinViewLoading()
                !viewState.isLoading && viewState.coinList.isNotEmpty() -> CoinViewDisplay(
                    viewState = viewState,
                    navController = navController,
                    onCoinClicked = remember {
                        {
                            scope.launch { scaffoldState.reveal() }
                            viewModel.obtainEvent(CoinEvent.CoinClick(it))
                        }
                    }
                )
                !viewState.isLoading -> Unit
                else -> throw NotImplementedError(
                    stringResource(id = R.string.crypto_implementation_state_error)
                )
            }
        })

    LaunchedEffect(Unit, block = {
        viewModel.obtainEvent(CoinEvent.FetchData)
    })
    LaunchedEffect(key1 = viewAction, block = {
        when (viewAction) {
            is CoinAction.OpenSimpleDetails -> Unit
            is CoinAction.CloseSimpleDetails -> Unit
            is CoinAction.OpenSearch -> navController.navigate(NavigationTree.Root.Search.name)
            is CoinAction.NavigateToDetails -> Unit
            null -> Unit
        }
    })
    LaunchedEffect(key1 = scaffoldState, block = {
        scaffoldState.reveal()
    })
}