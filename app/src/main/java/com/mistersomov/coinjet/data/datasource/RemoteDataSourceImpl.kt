package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.network.model.CoinInfoDto
import com.mistersomov.coinjet.data.service.CoinService
import com.mistersomov.coinjet.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val serviceHolder: CoinService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : RemoteDataSource {
    override fun fetchLatestCoinList(): Flow<List<CoinInfoDto>> {
        return flow {
            while (true) {
                val response = serviceHolder.getService().getTopCoinList()

                emit(checkNotNull(response.coinList))
                delay(DELAY)
            }
        }
            .flowOn(ioDispatcher)
    }

    companion object {
        private const val DELAY = 8000L
    }
}