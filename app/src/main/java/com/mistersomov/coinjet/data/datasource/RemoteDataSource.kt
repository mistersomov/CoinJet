package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.network.model.CoinInfoDto
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun fetchLatestCoinList(): Flow<List<CoinInfoDto>>
}