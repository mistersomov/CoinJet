package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.datasource.LocalDataSource
import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.toCoin
import com.mistersomov.coinjet.data.toCoinEntity
import com.mistersomov.coinjet.data.toSearchCoinEntity
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val remoteDataSourceImpl: RemoteDataSource,
    private val localDataSourceImpl: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : CoinRepository {
    override fun getLatestCoinList(): Flow<List<Coin>> {
        return remoteDataSourceImpl.getLatestCoinList()
            .onEach { coinList -> saveCoinListToCache(coinList) }
            .flowOn(defaultDispatcher)
    }

    override fun getCoinById(coinId: String): Flow<Coin> {
        return localDataSourceImpl.getCoinById(coinId)
            .map { entity -> entity.toCoin() }
            .flowOn(defaultDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun getCoinListBySearch(query: String): Flow<List<Coin>> {
        return when {
            query.isBlank() -> getRecentSearchList().mapLatest { list: List<Coin> ->
                when {
                    list.isEmpty() -> emptyList()
                    else -> list.sortedByDescending { coin -> coin.price.toDouble() }
                }
            }
                .flowOn(defaultDispatcher)

            else -> merge(getSearchSpecificCoinList(query), getSpecificCoinListFromCache(query))
                .mapLatest {
                    when {
                        it.isEmpty() -> emptyList()
                        else -> it.sortedByDescending { coin -> coin.price.toDouble() }
                    }
                }
                .debounce(300)
                .flowOn(defaultDispatcher)
        }
    }

    override suspend fun saveSearchCoinToCache(coin: Coin) {
        withContext(defaultDispatcher) {
            localDataSourceImpl.saveSearchCoinToCache(coin.toSearchCoinEntity())
        }
    }

    override fun getRecentSearchList(): Flow<List<Coin>> {
        return localDataSourceImpl.getRecentSearchList()
            .map { entityList -> entityList.map { entity -> entity.toCoin() } }
            .flowOn(defaultDispatcher)
    }

    override suspend fun clearSearchList() {
        withContext(defaultDispatcher) {
            localDataSourceImpl.clearSearchList()
        }
    }

    private suspend fun saveCoinListToCache(coinList: List<Coin>) {
        localDataSourceImpl.saveCoinListToCache(coinList.map { coin -> coin.toCoinEntity() })
    }

    private fun getCoinListFromCache(): Flow<List<Coin>> {
        return localDataSourceImpl.getCoinListFromCache().map { coinList ->
            coinList.map { coin ->
                coin.toCoin()
            }
        }
            .flowOn(defaultDispatcher)
    }

    private fun getSpecificCoinListFromCache(query: String): Flow<List<Coin>> {
        return localDataSourceImpl.getSpecificCoinList(query)
            .map { entityList -> entityList.map { entity -> entity.toCoin() } }
            .flowOn(defaultDispatcher)
    }

    private suspend fun clearCache() {
        withContext(defaultDispatcher) {
            localDataSourceImpl.deleteCoinListFromCache()
        }
    }

    private fun getSearchSpecificCoinList(query: String): Flow<List<Coin>> {
        return localDataSourceImpl.getRecentSearchSpecificCoinList(query).map { entityList ->
            entityList.map { entity -> entity.toCoin() }
        }
    }
}