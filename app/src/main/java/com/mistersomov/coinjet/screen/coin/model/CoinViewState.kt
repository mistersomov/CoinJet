package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.data.model.Coin

sealed class CoinViewState {

    data class Display(val coinList: List<Coin>) : CoinViewState()

    object Loading : CoinViewState()

    object NoItems : CoinViewState()

    object Error : CoinViewState()
}