package com.mistersomov.coinjet.screen.coin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.Padding
import com.mistersomov.coinjet.screen.coin.model.CoinEvent
import com.mistersomov.coinjet.screen.coin.model.CoinViewState
import com.mistersomov.coinjet.screen.coin.view.CoinViewDisplay
import com.mistersomov.coinjet.screen.coin.view.CoinViewLoading

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoinScreen(navController: NavController, viewModel: CoinViewModel = hiltViewModel()) {
    val viewState = viewModel.coinViewState.collectAsState()

    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = { },
        frontLayerScrimColor = Color.Unspecified,
        backLayerBackgroundColor = CoinJetTheme.colors.background,
        backLayerContent = {
            
        },
        frontLayerElevation = 10.dp,
        frontLayerBackgroundColor = CoinJetTheme.colors.background,
        frontLayerContent = {
            Surface(
                modifier = Modifier,
                color = CoinJetTheme.colors.surface,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(
                        modifier = Modifier
                            .width(72.dp)
                            .padding(top = 18.dp, bottom = 8.dp),
                        color = CoinJetTheme.colors.outline,
                        thickness = 2.dp
                    )
                    when (val currentState = viewState.value) {
                        is CoinViewState.Loading -> CoinViewLoading()
                        is CoinViewState.Display -> CoinViewDisplay(
                            padding = Padding(horizontal = 6),
                            navController = rememberNavController(),
                            viewState = currentState,
                        )
                        is CoinViewState.NoItems -> Unit
                        //is CryptoViewState.Error -> CryptoViewError()
                        else -> throw NotImplementedError(
                            stringResource(id = R.string.crypto_implementation_state_error)
                        )
                    }
                }
            }
        })

    LaunchedEffect(Unit, block = {
        viewModel.obtainEvent(CoinEvent.FetchData)
    })
}