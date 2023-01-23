package com.mistersomov.coinjet.screen.search.model

import com.mistersomov.coinjet.domain.model.Coin

data class SearchViewState(
    val recentSearchList: List<Coin> = emptyList(),
    val globalSearchList: List<Coin> = emptyList(),
    val isFirstSearch: Boolean = true,
)
