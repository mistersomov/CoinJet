package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.dao.FavoriteDao
import com.mistersomov.coinjet.data.database.dao.SearchCoinDao
import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.FavoriteDbModel
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel
import com.mistersomov.coinjet.data.toFavoriteDbModel
import com.mistersomov.coinjet.di.qualifier.IoDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao,
    private val searchCoinDao: SearchCoinDao,
    private val favoriteDao: FavoriteDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LocalDataSource {
    override suspend fun saveCoinListToCache(coinList: List<CoinInfoDbModel>) {
        withContext(ioDispatcher) {
            coinDao.insertAll(coinList)
        }
    }

    override suspend fun getCoinListFromCache(): List<CoinInfoDbModel> {
        return withContext(ioDispatcher) {
            coinDao.getAll()
        }
    }

    override fun getCoinById(id: String): Flow<CoinInfoDbModel> {
        return coinDao.getById(id).flowOn(ioDispatcher)
    }

    override suspend fun deleteCoinListFromCache() = withContext(ioDispatcher) {
        coinDao.deleteAll()
    }

    override suspend fun saveSearchCoinToCache(coin: SearchCoinDbModel) = withContext(ioDispatcher) {
        searchCoinDao.insertByEntity(coin)
    }

    override fun getRecentSearchList(): Flow<List<SearchCoinDbModel>> =
        searchCoinDao.getAll().flowOn(ioDispatcher)

    override suspend fun clearSearchList() = withContext(ioDispatcher) {
        searchCoinDao.deleteAll()
    }

    override suspend fun addCoinToFavorite(coin: Coin, currentTime: DateTime) {
        withContext(ioDispatcher) {
            favoriteDao.insert(coin.toFavoriteDbModel(currentTime))
        }
    }

    override fun getFavoriteList(): Flow<List<FavoriteDbModel>> {
        return favoriteDao.getAll().flowOn(ioDispatcher)
    }

    override suspend fun clearFavoriteList() {
        withContext(ioDispatcher) {
            favoriteDao.deleteAll()
        }
    }
}