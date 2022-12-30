package com.mistersomov.coinjet.screen.coin.model

sealed class CoinEvent {

    object FetchData : CoinEvent()

    data class CoinClick(val coinId: String) : CoinEvent()
}