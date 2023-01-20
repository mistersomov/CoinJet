package com.mistersomov.coinjet.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.use_case.coin.*
import com.mistersomov.coinjet.domain.use_case.favorite.AddToFavoriteUseCase
import com.mistersomov.coinjet.domain.use_case.favorite.ClearFavoriteListUseCase
import com.mistersomov.coinjet.domain.use_case.favorite.GetFavoriteListUseCase
import com.mistersomov.coinjet.domain.use_case.search.ClearSearchListUseCase
import com.mistersomov.coinjet.domain.use_case.search.GetCoinListBySearchUseCase
import com.mistersomov.coinjet.domain.use_case.search.GetRecentSearchListUseCase
import com.mistersomov.coinjet.domain.use_case.search.SaveSearchCoinToCacheUseCase
import com.mistersomov.coinjet.screen.coin.model.*
import com.mistersomov.coinjet.screen.coin.model.favorite.FavoriteViewState
import com.mistersomov.coinjet.screen.coin.model.search.SearchEvent
import com.mistersomov.coinjet.screen.coin.model.search.SearchViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val getCoinListFromCacheUseCase: GetCoinListFromCacheUseCase,
    private val getCoinByIdUseCase: GetCoinByIdUseCase,
    private val saveSearchCoinToCacheUseCase: SaveSearchCoinToCacheUseCase,
    private val getRecentSearchListUseCase: GetRecentSearchListUseCase,
    private val getCoinListBySearchUseCase: GetCoinListBySearchUseCase,
    private val clearSearchListUseCase: ClearSearchListUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    private val clearFavoriteListUseCase: ClearFavoriteListUseCase,
) : ViewModel() {

    private var _coinListViewState = MutableStateFlow<CoinListViewState>(CoinListViewState.Loading)
    val coinListViewState: StateFlow<CoinListViewState> = _coinListViewState

    private var _searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Hide)
    val searchViewState: StateFlow<SearchViewState> = _searchViewState

    private var _coinDetailsViewState =
        MutableStateFlow<CoinDetailsViewState>(CoinDetailsViewState.Hide)
    val coinDetailsViewState: StateFlow<CoinDetailsViewState> = _coinDetailsViewState

    private var _favoriteListViewState =
        MutableStateFlow<FavoriteViewState>(FavoriteViewState.Hide)
    val favoriteListViewState: StateFlow<FavoriteViewState> = _favoriteListViewState

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
    private var simpleDetailsJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }
    private var favoriteJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    fun obtainEvent(event: CoinEvent) {
        when (event) {
            is CoinEvent.FetchData -> fetchData()
            is CoinEvent.Click -> performCoinClick(event.id)
            is CoinEvent.HideSimpleDetails -> cancelSimpleDetailsJob()
            is CoinEvent.AddToFavorite -> addToFavorite(event.coin)
            is CoinEvent.ShowFavoriteList -> showFavoriteList()
            is CoinEvent.ClearFavoriteList -> clearFavoriteList()
        }
    }

    fun obtainSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Hide -> hideSearch()
            is SearchEvent.ShowRecentSearch -> showRecentSearch()
            is SearchEvent.LaunchSearch -> getCoinListBySearch(event.query)
            is SearchEvent.Save -> saveCoinToCache(event.coin)
            is SearchEvent.ClearCache -> deleteSearchList()
        }
    }

    private fun cancelSimpleDetailsJob() {
        hideSimpleDetails()
        simpleDetailsJob.cancel()
    }

    private fun cancelRecentSearchJob() {
        recentSearchJob.cancel()
    }

    private fun cancelSearchJob() {
        hideSearch()
        searchJob.cancel()
    }

    private fun hideSearch() {
        _searchViewState.value = SearchViewState.Hide
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchDataUseCase()
                .cancellable()
                .onStart {
                    if (getCoinListFromCacheUseCase().isNotEmpty()) {
                        _coinListViewState.value =
                            CoinListViewState.Display(coinList = getCoinListFromCacheUseCase())
                    }
                    showFavoriteList()
                }
                .collect { coinList ->
                    when {
                        coinList.isNotEmpty() -> _coinListViewState.value =
                            CoinListViewState.Display(coinList = coinList)
                        else -> _coinListViewState.value = CoinListViewState.NoItems
                    }
                }
        }
    }

    private fun performCoinClick(id: String) {
        if (simpleDetailsJob.isActive) {
            simpleDetailsJob.cancel()
        }
        viewModelScope.launch(simpleDetailsJob) {
            while (isActive) {
                getCoinByIdUseCase(id)
                    .cancellable()
                    .collect { coin ->
                        _coinDetailsViewState.value = CoinDetailsViewState.SimpleDetails(coin)
                        if (searchJob.isActive) {
                            cancelSearchJob()
                        }
                    }
            }
        }
    }

    private fun hideSimpleDetails() {
        _coinDetailsViewState.value = CoinDetailsViewState.Hide
    }

    private fun showRecentSearch() {
        viewModelScope.launch(recentSearchJob) {
            getRecentSearchListUseCase().onCompletion {
                if (simpleDetailsJob.isActive) {
                    cancelSimpleDetailsJob()
                }
            }.collect { searchList ->
                when {
                    searchList.isNotEmpty() -> _searchViewState.value =
                        SearchViewState.Recent(recentSearchList = searchList)
                    else -> _searchViewState.value = SearchViewState.NoItems
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun getCoinListBySearch(query: String) {
        viewModelScope.launch(searchJob) {
            if (query.isBlank()) {
                cancelSearchJob()
            } else {
                flow {
                    emit(query)
                    delay(DELAY_SEARCH)
                }
                    .cancellable()
                    .onStart {
                        if (simpleDetailsJob.isActive) {
                            cancelSimpleDetailsJob()
                        }
                    }
                    .debounce(DELAY_SEARCH)
                    .collect { query ->
                        val searchList = getCoinListBySearchUseCase(query)

                        _searchViewState.value = when {
                            searchList.isNotEmpty() ->
                                SearchViewState.Global(globalSearchList = searchList)
                            else -> SearchViewState.NoItems
                        }
                    }
            }
        }
    }

    private fun saveCoinToCache(coin: Coin) {
        cancelRecentSearchJob()
        viewModelScope.launch {
            val currentTime = DateTime.now()
            saveSearchCoinToCacheUseCase(coin, currentTime)
        }
    }

    private fun deleteSearchList() {
        viewModelScope.launch {
            clearSearchListUseCase()
        }
    }

    private fun addToFavorite(coin: Coin) {
        viewModelScope.launch {
            val currentTime = DateTime.now()
            addToFavoriteUseCase(coin, currentTime)
        }
    }

    private fun showFavoriteList() {
        viewModelScope.launch {
            getFavoriteListUseCase()
                .cancellable()
                .collect { favoriteList ->
                    when {
                        favoriteList.isNotEmpty() -> _favoriteListViewState.value =
                            FavoriteViewState.Display(favoriteCoinList = favoriteList)
                        else -> _favoriteListViewState.value = FavoriteViewState.Hide
                    }
                }
        }
    }

    private fun clearFavoriteList() {
        viewModelScope.launch {
            clearFavoriteListUseCase()
        }
    }

    companion object {
        private const val DELAY_SEARCH = 500L
    }
}