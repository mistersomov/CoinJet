package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.entity.CoinEntity
import com.mistersomov.coinjet.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val coinDao: CoinDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun saveCoinListToCache(coinList: List<CoinEntity>) = withContext(ioDispatcher) {
        coinDao.insertAll(coinList)
    }

    fun getCoinListFromCache(): Flow<List<CoinEntity>> = coinDao.getAll().flowOn(ioDispatcher)

    suspend fun deleteCoinListFromCache() = withContext(ioDispatcher) {
        coinDao.deleteAll()
    }
}