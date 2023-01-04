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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : CoinRepository {

    override fun getLatestCoinList(): Flow<List<Coin>> {
        return remoteDataSource.getLatestCoinList()
            .onEach { coinList -> saveCoinListToCache(coinList) }
            .flowOn(defaultDispatcher)
    }

    override fun getCoinById(coinId: String): Flow<Coin> {
        return localDataSource.getCoinById(coinId)
            .map { entity -> entity.toCoin() }
            .flowOn(defaultDispatcher)
    }

    override fun getRecentSearchList(): Flow<List<Coin>> {
        return localDataSource.getRecentSearchList()
            .map { entityList ->
                when {
                    entityList.isEmpty() -> emptyList()
                    else -> entityList
                        .map { entity -> entity.toCoin() }
                        .sortedByDescending { coin -> coin.mktCap }
                }
            }
            .flowOn(defaultDispatcher)
    }

    override suspend fun getCoinListByName(name: String): List<Coin> {
        return withContext(defaultDispatcher) {
            getCoinListFromCache()
                .sortedBy { calculateLevenstain(name.lowercase(), it.fullName.lowercase()) }
                .distinctBy { it.symbol }
        }
    }

    override suspend fun getCoinListBySymbol(symbol: String): List<Coin> {
        return withContext(defaultDispatcher) {
            getCoinListFromCache()
                .sortedBy { calculateLevenstain(symbol.lowercase(), it.symbol.lowercase()) }
                .distinctBy { it.symbol }
        }
    }

    override suspend fun saveSearchCoinToCache(coin: Coin) {
        withContext(defaultDispatcher) {
            localDataSource.saveSearchCoinToCache(coin.toSearchCoinEntity())
        }
    }

    override suspend fun clearSearchList() {
        withContext(defaultDispatcher) {
            localDataSource.clearSearchList()
        }
    }

    private suspend fun getCoinListFromCache(): List<Coin> {
        return localDataSource.getCoinListFromCache().map { it.toCoin() }
    }

    private suspend fun clearCache() {
        withContext(defaultDispatcher) {
            localDataSource.deleteCoinListFromCache()
        }
    }

    private suspend fun saveCoinListToCache(coinList: List<Coin>) {
        localDataSource.saveCoinListToCache(coinList.map { coin -> coin.toCoinEntity() })
    }

    private fun calculateLevenstain(query: String, coinName: String): Int {
        val di1 = IntArray(coinName.length + 1)
        val di = IntArray(coinName.length + 1)
        for (j in 0..coinName.length) {
            di[j] = j
        }
        for (i in 1..query.length) {
            System.arraycopy(di, 0, di1, 0, di1.size)
            di[0] = i
            for (j in 1..coinName.length) {
                val cost = if (query[i - 1] != coinName[j - 1]) 1 else 0
                di[j] = (di1[j] + 1)
                    .coerceAtMost(di[j - 1] + 1)
                    .coerceAtMost(di1[j - 1] + cost)
            }
        }
        return di[di.size - 1]
    }
}