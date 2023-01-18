package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.domain.model.Coin

sealed class CoinListViewState {

    data class Display(val coinList: List<Coin>) : CoinListViewState()

    object Loading : CoinListViewState()

    object NoItems : CoinListViewState()

    object Error : CoinListViewState()
}