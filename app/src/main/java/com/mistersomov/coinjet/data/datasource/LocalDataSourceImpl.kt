package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.dao.SearchCoinDao
import com.mistersomov.coinjet.data.database.entity.CoinEntity
import com.mistersomov.coinjet.data.database.entity.SearchCoinEntity
import com.mistersomov.coinjet.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao,
    private val searchCoinDao: SearchCoinDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocalDataSource {
    override suspend fun saveCoinListToCache(coinList: List<CoinEntity>) = withContext(ioDispatcher) {
        coinDao.insertAll(coinList)
    }

    override fun getCoinListFromCache(): Flow<List<CoinEntity>> =
        coinDao.getAll().flowOn(ioDispatcher)

    override fun getCoinById(coinId: String): Flow<CoinEntity> =
        coinDao.getById(coinId).flowOn(ioDispatcher)

    override fun getSpecificCoinList(query: String): Flow<List<CoinEntity>> =
        coinDao.getAllByName(query).flowOn(ioDispatcher)

    override suspend fun deleteCoinListFromCache() = withContext(ioDispatcher) {
        coinDao.deleteAll()
    }

    override suspend fun saveSearchCoinToCache(coin: SearchCoinEntity) = withContext(ioDispatcher) {
        searchCoinDao.insertByEntity(coin)
    }

    override fun getRecentSearchList(): Flow<List<SearchCoinEntity>> =
        searchCoinDao.getAll().flowOn(ioDispatcher)

    override fun getRecentSearchSpecificCoinList(query: String): Flow<List<SearchCoinEntity>> =
        searchCoinDao.getAllByName(query).flowOn(ioDispatcher)

    override suspend fun clearSearchList() = withContext(ioDispatcher) {
        searchCoinDao.deleteAll()
    }
}