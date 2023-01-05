package com.mistersomov.coinjet.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.use_case.coin.*
import com.mistersomov.coinjet.screen.coin.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val getCoinBySymbolUseCase: GetCoinBySymbolUseCase,
    private val saveCoinToCacheUseCase: SaveCoinToCacheUseCase,
    private val getRecentSearchListUseCase: GetRecentSearchListUseCase,
    private val getCoinListBySearchUseCase: GetCoinListBySearchUseCase,
    private val clearSearchListUseCase: ClearSearchListUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _coinViewState = MutableStateFlow<CoinViewState>(CoinViewState.Loading)
    val coinViewState: StateFlow<CoinViewState> = _coinViewState

    private var _searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Hide)
    val searchViewState: StateFlow<SearchViewState> = _searchViewState

    private var _coinDetailsViewState =
        MutableStateFlow<CoinDetailsViewState>(CoinDetailsViewState.Hide)
    val coinDetailsViewState: StateFlow<CoinDetailsViewState> = _coinDetailsViewState

    private var searchJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }
    private var simpleDetailsJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    fun obtainEvent(event: CoinEvent) {
        when (event) {
            is CoinEvent.FetchData -> fetchData()
            is CoinEvent.Click -> performCoinClick(event.symbol)
        }
    }

    fun obtainSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Hide -> hideSearch()
            is SearchEvent.Click -> showRecentSearch()
            is SearchEvent.LaunchSearch -> getCoinListBySearch(event.query)
            is SearchEvent.Save -> saveCoinToCache(event.coin)
            is SearchEvent.ClearCache -> deleteSearchList()
        }
    }

    fun cancelSimpleDetailsJob() {
        hideSimpleDetails()
        simpleDetailsJob.cancel()
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchDataUseCase().collect { coinList ->
                when {
                    coinList.isEmpty() -> _coinViewState.value = CoinViewState.NoItems
                    else -> _coinViewState.value = CoinViewState.Display(coinList = coinList)
                }
            }
        }
    }

    private fun performCoinClick(symbol: String) {
        if (_searchViewState.value !is SearchViewState.Hide) {
            hideSearch()
        }
        if (_coinDetailsViewState.value is CoinDetailsViewState.SimpleDetails) {
            simpleDetailsJob.cancel()
        }
        viewModelScope.launch(simpleDetailsJob) {
            while (isActive) {
                getCoinBySymbolUseCase(symbol).collect { coin ->
                    _coinDetailsViewState.value = CoinDetailsViewState.SimpleDetails(coin)
                }
            }
        }
    }

    fun hideSearch() {
        _searchViewState.value = SearchViewState.Hide
    }

    private fun hideSimpleDetails() {
        _coinDetailsViewState.value = CoinDetailsViewState.Hide
    }

    private fun showRecentSearch() {
        viewModelScope.launch {
            getRecentSearchListUseCase().collect { searchList ->
                when {
                    searchList.isEmpty() -> _searchViewState.value = SearchViewState.FirstSearch
                    else -> _searchViewState.value =
                        SearchViewState.Recent(recentSearchList = searchList)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun getCoinListBySearch(query: String) {
        viewModelScope.launch(searchJob + defaultDispatcher) {
            if (query.isBlank()) {
                showRecentSearch()
            } else {
                flow {
                    emit(query)
                    delay(DELAY_SEARCH)
                }
                    .debounce(DELAY_SEARCH)
                    .collect {
                        val searchList = getCoinListBySearchUseCase(it)

                        _searchViewState.value = when {
                            searchList.isEmpty() -> SearchViewState.NoItems
                            else -> SearchViewState.Global(globalSearchList = searchList)
                        }
                    }
            }
        }
    }

    private fun saveCoinToCache(coin: Coin) {
        viewModelScope.launch {
            saveCoinToCacheUseCase(coin)
            _searchViewState.value = SearchViewState.Hide
        }
    }

    private fun deleteSearchList() {
        viewModelScope.launch {
            clearSearchListUseCase()
        }
    }

    companion object {
        private const val DELAY_SEARCH = 500L
    }
}