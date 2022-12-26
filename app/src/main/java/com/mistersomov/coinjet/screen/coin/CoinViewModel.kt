package com.mistersomov.coinjet.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mistersomov.coinjet.data.repository.CoinRepository
import com.mistersomov.coinjet.screen.coin.model.CoinEvent
import com.mistersomov.coinjet.screen.coin.model.CoinViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repository: CoinRepository,
) : ViewModel() {

    private var _coinViewState = MutableStateFlow<CoinViewState>(CoinViewState.Loading)
    val coinViewState: StateFlow<CoinViewState> = _coinViewState

    fun obtainEvent(event: CoinEvent) {
        when (event) {
            is CoinEvent.FetchData -> fetchData()
            else -> Unit
        }
    }

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
}