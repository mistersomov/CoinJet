package com.mistersomov.coinjet.data.repository

import com.mistersomov.coinjet.data.datasource.RemoteDataSource
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    val latestCoinList: Flow<List<Coin>> = remoteDataSource.latestCoinList
        .flowOn(defaultDispatcher)

}