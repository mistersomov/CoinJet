package com.mistersomov.coinjet.data.datasource

import com.mistersomov.coinjet.data.mapResponseToCoinList
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.service.CoinService
import com.mistersomov.coinjet.data.toQuoteList
import com.mistersomov.coinjet.data.toSimpleCoinString
import com.mistersomov.coinjet.di.qualifier.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val serviceHolder: CoinService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    val latestCoinList: Flow<List<Coin>> = flow {
        while (true) {
            val topCoinList = serviceHolder.getService().getTopCoinList()
            val quoteList = serviceHolder.getService()
                .getQuoteList(fromSymbols = topCoinList.toSimpleCoinString())
                .toQuoteList()
            val resultCoinList = mapResponseToCoinList(quoteList, topCoinList)

            emit(resultCoinList)
            delay(DELAY)
        }
    }
        .flowOn(ioDispatcher)

    companion object {
        private const val DELAY = 8000L
    }
}