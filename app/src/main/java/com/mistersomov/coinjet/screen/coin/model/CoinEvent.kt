package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.domain.model.Coin

sealed class CoinEvent {

    object FetchData : CoinEvent()

    data class Click(val id: String) : CoinEvent()

    object HideSimpleDetails : CoinEvent()

    data class AddToFavorite(val coin: Coin) : CoinEvent()

    object ShowFavoriteList : CoinEvent()

    object ClearFavoriteList : CoinEvent()
}