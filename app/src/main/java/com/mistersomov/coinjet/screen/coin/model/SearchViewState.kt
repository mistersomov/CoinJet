package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.data.model.Coin

sealed class SearchViewState {

    object Hide : SearchViewState()

    data class Display(val searchList: List<Coin>) : SearchViewState()
}