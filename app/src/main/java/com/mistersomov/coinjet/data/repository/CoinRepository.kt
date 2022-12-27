package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.datasource.LocalDataSource
import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.toCoin
import com.mistersomov.coinjet.data.toCoinEntity
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

    private suspend fun clearCache() = withContext(defaultDispatcher) {
        localDataSource.deleteCoinListFromCache()
    }
}