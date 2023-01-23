package com.mistersomov.coinjet.screen.coin.model

sealed class CoinEvent {

    object FetchData : CoinEvent()

    data class CoinClick(val id: String) : CoinEvent()

    object SimpleDetailsClose: CoinEvent()

    object SearchClick : CoinEvent()
}