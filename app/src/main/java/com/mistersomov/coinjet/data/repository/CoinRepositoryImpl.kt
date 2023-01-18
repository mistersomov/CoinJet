package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.FavoriteDbModel
import com.mistersomov.coinjet.data.datasource.LocalDataSource
import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.toDbModel
import com.mistersomov.coinjet.data.toEntity
import com.mistersomov.coinjet.data.toSearchCoinDbModel
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : CoinRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchLatestCoinList(): Flow<List<Coin>> {
        return remoteDataSource.fetchLatestCoinList()
            .mapLatest { dtoList -> dtoList.map { it.toDbModel() } }
            .onEach { dbList -> saveCoinListToCache(dbList) }
            .mapLatest { dbList -> dbList.map { it.toEntity() } }
            .flowOn(defaultDispatcher)
    }

    override suspend fun getCoinListFromCache(): List<Coin> {
        return withContext(defaultDispatcher) {
            localDataSource.getCoinListFromCache().map { it.toEntity() }
        }
    }

    override fun getCoinById(id: String): Flow<Coin> {
        return localDataSource.getCoinById(id)
            .map { entity -> entity.toEntity() }
            .flowOn(defaultDispatcher)
    }

    override fun getRecentSearchList(): Flow<List<Coin>> {
        return localDataSource.getRecentSearchList()
            .map { dbModelList ->
                when {
                    dbModelList.isNotEmpty() -> dbModelList
                        .distinctBy { it.symbol }
                        .sortedByDescending { dbModel -> dbModel.time }
                        .map { dbModel -> dbModel.toEntity() }
                    else -> emptyList()
                }
            }
            .flowOn(defaultDispatcher)
    }

    override suspend fun getCoinListByName(name: String): List<Coin> {
        return withContext(defaultDispatcher) {
            getCoinListFromCache()
                .filter { it.name.lowercase().contains(name.lowercase()) }
                .sortedBy { it.rank }
                .distinctBy { it.symbol }
        }
    }

    override suspend fun getCoinListBySymbol(symbol: String): List<Coin> {
        return withContext(defaultDispatcher) {
            getCoinListFromCache()
                .filter { it.symbol.lowercase().contains(symbol.lowercase()) }
                .sortedBy { it.rank }
                .distinctBy { it.symbol }
        }
    }

    override suspend fun saveSearchCoinToCache(coin: Coin, time: DateTime) {
        withContext(defaultDispatcher) {
            localDataSource.saveSearchCoinToCache(coin.toSearchCoinDbModel(time))
        }
    }

    override suspend fun clearSearchList() {
        withContext(defaultDispatcher) {
            localDataSource.clearSearchList()
        }
    }

    private suspend fun clearCache() {
        withContext(defaultDispatcher) {
            localDataSource.deleteCoinListFromCache()
        }
    }

    private suspend fun saveCoinListToCache(coinList: List<CoinInfoDbModel>) {
        localDataSource.saveCoinListToCache(coinList)
    }

    override suspend fun addCoinToFavorite(coin: Coin, currentTime: DateTime) {
        withContext(defaultDispatcher) {
            localDataSource.addCoinToFavorite(coin, currentTime)
        }
    }

    override fun getFavoriteList(): Flow<List<Coin>> {
        return localDataSource.getFavoriteList()
            .map { dbList ->
                when {
                    dbList.isNotEmpty() -> dbList
                        .sortedByDescending { it.time }
                        .map { it.toEntity() }
                    else -> emptyList()
                }
            }
            .flowOn(defaultDispatcher)
    }

    override suspend fun clearFavoriteList() {
        withContext(defaultDispatcher) {
            localDataSource.clearFavoriteList()
        }
    }
}