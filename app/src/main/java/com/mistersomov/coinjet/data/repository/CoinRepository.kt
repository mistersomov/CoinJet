package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.datasource.LocalDataSource
import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.toCoin
import com.mistersomov.coinjet.data.toCoinEntity
import com.mistersomov.coinjet.data.toSearchCoinEntity
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    val latestCoinList: Flow<List<Coin>> = remoteDataSource.latestCoinList
        .onEach { coinList -> saveCoinListToCache(coinList) }
        .flowOn(defaultDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun searchCoin(query: String): Flow<List<Coin>> {
        val formattedQuery = buildString {
            append("%")
            append(query)
            append("%")
        }

        return when {
            query.isBlank() -> getSearchList().mapLatest { list: List<Coin> ->
                when {
                    list.isEmpty() -> emptyList()
                    else -> list.sortedByDescending { coin -> coin.price.toDouble() }
                }
            }
                .flowOn(defaultDispatcher)

            else -> merge(getSearchSpecificCoinList(formattedQuery), getSpecificCoinListFromCache(
                formattedQuery
            )).mapLatest {
                when {
                    it.isEmpty() -> emptyList()
                    else -> it.sortedByDescending { coin -> coin.price.toDouble() }
                }
            }
                .debounce(300)
                .flowOn(defaultDispatcher)
        }
    }

    suspend fun saveSearchCoinToCache(coin: Coin) = withContext(defaultDispatcher) {
        localDataSource.saveSearchCoinToCache(coin.toSearchCoinEntity())
    }

    fun getSearchList(): Flow<List<Coin>> = localDataSource.getRecentSearchList()
        .map { entityList -> entityList.map { entity -> entity.toCoin() } }
        .flowOn(defaultDispatcher)

    suspend fun clearSearchList() = withContext(defaultDispatcher) {
        localDataSource.clearSearchList()
    }

    private suspend fun saveCoinListToCache(coinList: List<Coin>) {
        localDataSource.saveCoinListToCache(coinList.map { coin -> coin.toCoinEntity() })
    }

    private fun getCoinListFromCache(): Flow<List<Coin>> {
        return localDataSource.getCoinListFromCache().map { coinList ->
            coinList.map { coin ->
                coin.toCoin()
            }
        }
            .flowOn(defaultDispatcher)
    }

    private fun getSpecificCoinListFromCache(query: String): Flow<List<Coin>> =
        localDataSource.getSpecificCoinList(query)
            .map { entityList -> entityList.map { entity -> entity.toCoin() } }
            .flowOn(defaultDispatcher)

    private suspend fun clearCache() = withContext(defaultDispatcher) {
        localDataSource.deleteCoinListFromCache()
    }

    private fun getSearchSpecificCoinList(query: String): Flow<List<Coin>> =
        localDataSource.getRecentSearchSpecificCoinList(query).map { entityList ->
            entityList.map { entity -> entity.toCoin() }
        }
}