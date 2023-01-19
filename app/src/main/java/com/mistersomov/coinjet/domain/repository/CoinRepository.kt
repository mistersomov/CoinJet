package com.mistersomov.coinjet.domain.repository

import com.mistersomov.coinjet.domain.model.Coin
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

interface CoinRepository {
    fun fetchLatestCoinList(): Flow<List<Coin>>

    suspend fun getCoinListFromCache(): List<Coin>

    fun getCoinById(id: String): Flow<Coin>

    suspend fun getCoinListByName(name: String): List<Coin>

    suspend fun getCoinListBySymbol(symbol: String): List<Coin>

    suspend fun saveSearchCoinToCache(coin: Coin, time: DateTime)

    fun getRecentSearchList(): Flow<List<Coin>>

    suspend fun clearSearchList()
}