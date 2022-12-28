package com.mistersomov.coinjet.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.repository.CoinRepository
import com.mistersomov.coinjet.screen.coin.model.CoinEvent
import com.mistersomov.coinjet.screen.coin.model.CoinViewState
import com.mistersomov.coinjet.screen.coin.model.SearchEvent
import com.mistersomov.coinjet.screen.coin.model.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repository: CoinRepository,
) : ViewModel() {

    private var _coinViewState = MutableStateFlow<CoinViewState>(CoinViewState.Loading)
    val coinViewState: StateFlow<CoinViewState> = _coinViewState

    private var _searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Hide)
    val searchViewState: StateFlow<SearchViewState> = _searchViewState

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    fun obtainEvent(event: CoinEvent) {
        when (event) {
            is CoinEvent.FetchData -> fetchData()
            else -> Unit
        }
    }

    fun obtainSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchClick -> performSearchClick()
            is SearchEvent.LaunchSearch -> launchSearch(event.query)
            is SearchEvent.SaveCoin -> saveCoinToCache(event.coin)
            is SearchEvent.ClearCache -> deleteSearchList()
        }
    }

    fun completeJob() = job.complete()

    private fun fetchData() {
        viewModelScope.launch(CoroutineName("fetchDataCoroutine")) {
            repository.latestCoinList.collect { coinList ->
                if (coinList.isEmpty()) {
                    _coinViewState.value = CoinViewState.NoItems
                } else {
                    _coinViewState.value = CoinViewState.Display(coinList = coinList)
                }
            }
        }
    }

    private fun performSearchClick() {
        viewModelScope.launch {
            repository.getSearchList().collect { searchList ->
                when {
                    searchList.isEmpty() -> {
                        _searchViewState.value = SearchViewState.Display(searchList = emptyList())
                    }
                    else -> {
                        _searchViewState.value = SearchViewState.Display(searchList = searchList)
                    }
                }
            }
        }
    }

    private fun launchSearch(query: String) {
        viewModelScope.launch(job + Dispatchers.Default) {
            val list = repository.searchCoin(query)
            _searchViewState.value = when (list.isEmpty()) {
                true -> SearchViewState.Display(searchList = emptyList())
                false -> SearchViewState.Display(searchList = list)
            }
        }
    }

    private fun saveCoinToCache(coin: Coin) {
        viewModelScope.launch(CoroutineName("saveCoinToCache")) {
            repository.saveSearchCoinToCache(coin)
        }
    }

    private fun deleteSearchList() {
        viewModelScope.launch {
            repository.clearSearchList()
        }
    }
}