package com.mistersomov.coinjet.domain.use_case.search

import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import javax.inject.Inject

class SaveSearchCoinToCacheUseCase @Inject constructor(
    private val repository: CoinRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(coin: Coin, time: DateTime) {
        withContext(defaultDispatcher) {
            repository.saveSearchCoinToCache(coin, time)
        }
    }
}