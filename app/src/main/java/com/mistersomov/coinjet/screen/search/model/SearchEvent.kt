package com.mistersomov.coinjet.screen.search.model

import com.mistersomov.coinjet.domain.model.Coin

sealed class SearchEvent {

    object Hide : SearchEvent()

    object ShowRecentSearch : SearchEvent()

    data class StartSearching(val query: String) : SearchEvent()

    data class Click(val coin: Coin) : SearchEvent()

    object ClearCache : SearchEvent()
}