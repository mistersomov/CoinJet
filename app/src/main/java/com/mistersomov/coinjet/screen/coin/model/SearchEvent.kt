package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.data.model.Coin

sealed class SearchEvent {

    object SearchClick : SearchEvent()

    data class LaunchSearch(val query: String) : SearchEvent()

    data class SaveCoin(val coin: Coin) : SearchEvent()

    object ClearCache : SearchEvent()
}