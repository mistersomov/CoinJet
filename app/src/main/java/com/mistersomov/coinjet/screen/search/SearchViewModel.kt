package com.mistersomov.coinjet.screen.search

import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.base.BaseViewModel
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.use_case.search.ClearSearchListUseCase
import com.mistersomov.coinjet.domain.use_case.search.GetCoinListBySearchUseCase
import com.mistersomov.coinjet.domain.use_case.search.GetRecentSearchListUseCase
import com.mistersomov.coinjet.domain.use_case.search.SaveSearchCoinToCacheUseCase
import com.mistersomov.coinjet.screen.search.model.SearchAction
import com.mistersomov.coinjet.screen.search.model.SearchEvent
import com.mistersomov.coinjet.screen.search.model.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecentSearchListUseCase: GetRecentSearchListUseCase,
    private val getCoinListBySearchUseCase: GetCoinListBySearchUseCase,
    private val saveSearchUseCase: SaveSearchCoinToCacheUseCase,
    private val clearSearchListUseCase: ClearSearchListUseCase,
) : BaseViewModel<SearchViewState, SearchAction, SearchEvent>(SearchViewState()) {

    private var recentSearchJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }
    private var searchJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    override fun obtainEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Hide -> hideSearch()
            is SearchEvent.StartSearching -> performSearch(event.query)
            is SearchEvent.ShowRecentSearch -> showRecentSearch()
            is SearchEvent.Click -> performClick(event.coin)
            is SearchEvent.ClearCache -> clearCache()
        }
    }

    private fun hideSearch() {
        viewAction = SearchAction.Hide
    }

    @OptIn(FlowPreview::class)
    private fun performSearch(query: String) {
        viewModelScope.launch(searchJob) {
            if (query.isBlank()) {
                cancelSearchJob()
            } else {
                flow {
                    emit(query)
                    delay(DELAY_SEARCH)
                }
                    .cancellable()
                    .debounce(DELAY_SEARCH)
                    .collect { query ->
                        val searchList = getCoinListBySearchUseCase(query)

                        viewState = when {
                            searchList.isNotEmpty() -> viewState.copy(globalSearchList = searchList)
                            else -> viewState.copy(globalSearchList = emptyList())
                        }
                    }
            }
        }
    }

    private fun showRecentSearch() {
        viewModelScope.launch(recentSearchJob) {
            getRecentSearchListUseCase().collect { searchList ->
                viewState = when {
                    searchList.isNotEmpty() -> viewState.copy(recentSearchList = searchList)
                    else -> viewState.copy(recentSearchList = emptyList())
                }
            }
        }
    }

    private fun performClick(coin: Coin) {
        saveSearch(coin)
    }

    private fun saveSearch(coin: Coin) {
        viewModelScope.launch {
            val currentTime = DateTime.now()
            saveSearchUseCase(coin = coin, time = currentTime)
        }
    }

    private fun clearCache() {
        viewModelScope.launch {
            clearSearchListUseCase()
        }
    }

    private fun cancelSearchJob() {
        searchJob.cancel()
    }

    companion object {
        private const val DELAY_SEARCH = 500L
    }
}