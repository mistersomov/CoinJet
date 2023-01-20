package com.mistersomov.coinjet.screen.coin.model.favorite

import com.mistersomov.coinjet.domain.model.Coin

sealed class FavoriteViewState {
    data class Display(val favoriteCoinList: List<Coin>) : FavoriteViewState()

    object Hide : FavoriteViewState()
}
