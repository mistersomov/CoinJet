package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.datasource.LocalDataSource
import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.toCoin
import com.mistersomov.coinjet.data.toCoinEntity
import com.mistersomov.coinjet.data.toSearchCoinEntity
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
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

    suspend fun searchCoin(query: String): List<Coin> {
        val formattedQuery = buildString {
            append("%")
            append(query)
            append("%")
        }
        return getSpecificCoinListFromCache(formattedQuery)

//        return when {
//            query.isBlank() -> flow {
//                    getSearchList().collect {
//                        emit(it)
//                    }
//                }
//                    .map { list: List<Coin> ->
//                        when {
//                            list.isEmpty() -> emptyList()
//                            else -> list
//                        }
//                    }
//                    .flowOn(defaultDispatcher)
//
//            else -> flow {
//                    getSpecificCoinListFromCache(formattedQuery).collect {
//                        emit(it)
//                    }
//                }
//                    .map {
//                        when {
//                            it.isEmpty() -> emptyList()
//                            else -> it
//                        }
//                    }
//                    .flowOn(defaultDispatcher)
//            }
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

    private suspend fun getSpecificCoinListFromCache(query: String): List<Coin> =
        withContext(defaultDispatcher) {
            localDataSource.getSpecificCoinList(query).map { entity -> entity.toCoin() }
        }

    private suspend fun clearCache() = withContext(defaultDispatcher) {
        localDataSource.deleteCoinListFromCache()
    }

    suspend fun saveSearchCoinToCache(coin: Coin) = withContext(defaultDispatcher) {
        localDataSource.saveSearchCoinToCache(coin.toSearchCoinEntity())
    }

    fun getSearchList(): Flow<List<Coin>> = localDataSource.getRecentSearchList()
        .map { searchList -> searchList.map { entity -> entity.toCoin() } }
        .flowOn(defaultDispatcher)


    private suspend fun getSearchSpecificCoinList(query: String): List<Coin> =
        localDataSource.getRecentSearchSpecificCoinList(query).map { entity -> entity.toCoin() }

    suspend fun clearSearchList() = withContext(defaultDispatcher) {
        localDataSource.clearSearchList()
    }
}