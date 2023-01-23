package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.domain.model.Coin

data class CoinViewState(
    val coinList: List<Coin> = emptyList(),
    val favoriteList: List<Coin> = emptyList(),
    val details: Coin? = null,
    val isLoading: Boolean = true,
)