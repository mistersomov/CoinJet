package com.mistersomov.coinjet.screen.coin.model

import com.mistersomov.coinjet.domain.model.Coin

sealed class CoinAction {

    object OpenSimpleDetails : CoinAction()

    object CloseSimpleDetails : CoinAction()

    object NavigateToDetails : CoinAction()

    object OpenSearch : CoinAction()
}