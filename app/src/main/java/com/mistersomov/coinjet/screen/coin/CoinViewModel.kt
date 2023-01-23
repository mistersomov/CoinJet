package com.mistersomov.coinjet.screen.coin

import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.base.BaseViewModel
import com.mistersomov.coinjet.domain.use_case.coin.FetchDataUseCase
import com.mistersomov.coinjet.domain.use_case.coin.GetCoinByIdUseCase
import com.mistersomov.coinjet.domain.use_case.coin.GetCoinListFromCacheUseCase
import com.mistersomov.coinjet.screen.coin.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
    private val getCoinListFromCacheUseCase: GetCoinListFromCacheUseCase,
    private val getCoinByIdUseCase: GetCoinByIdUseCase,
) : BaseViewModel<CoinViewState, CoinAction, CoinEvent>(CoinViewState()) {

    private var simpleDetailsJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    override fun obtainEvent(event: CoinEvent) {
        when (event) {
            is CoinEvent.FetchData -> fetchData()
            is CoinEvent.CoinClick -> performCoinClick(event.id)
            is CoinEvent.SimpleDetailsClose -> closeSimpleDetails()
            is CoinEvent.SearchClick -> performSearchClick()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchDataUseCase()
                .cancellable()
                .onStart {
                    if (getCoinListFromCacheUseCase().isNotEmpty()) {
                        viewState = viewState.copy(
                            coinList = getCoinListFromCacheUseCase(),
                            isLoading = false
                        )
                    }
                }
                .collect { coinList ->
                    viewState = when {
                        coinList.isNotEmpty() -> viewState.copy(
                            coinList = coinList,
                            isLoading = false
                        )
                        else -> viewState.copy(coinList = emptyList(), isLoading = false)
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
                        viewState = viewState.copy(details = coin)
                    }
            }
        }
    }

    private fun closeSimpleDetails() {
        simpleDetailsJob.cancel()
        viewState = viewState.copy(details = null)
    }

    private fun performSearchClick() {
        viewModelScope.launch {
            viewAction = CoinAction.OpenSearch
        }
    }

    private fun showSimpleDetails() {
        viewAction = CoinAction.OpenSimpleDetails
    }

    private fun hideSimpleDetails() {
        viewAction = CoinAction.CloseSimpleDetails
    }
}