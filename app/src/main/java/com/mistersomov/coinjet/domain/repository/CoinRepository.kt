package com.mistersomov.coinjet.domain.repository

import com.mistersomov.coinjet.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getLatestCoinList(): Flow<List<Coin>>

    fun getCoinById(coinId: String): Flow<Coin>

    fun getCoinListBySearch(query: String): Flow<List<Coin>>

    suspend fun saveSearchCoinToCache(coin: Coin)

    fun getRecentSearchList(): Flow<List<Coin>>

    suspend fun clearSearchList()
}