package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getCoinListFromCache(): List<CoinInfoDbModel>

    fun getCoinById(id: String): Flow<CoinInfoDbModel>

    suspend fun saveCoinListToCache(coinList: List<CoinInfoDbModel>)

    suspend fun deleteCoinListFromCache()

    suspend fun saveSearchCoinToCache(coin: SearchCoinDbModel)

    fun getRecentSearchList(): Flow<List<SearchCoinDbModel>>

    suspend fun clearSearchList()
}