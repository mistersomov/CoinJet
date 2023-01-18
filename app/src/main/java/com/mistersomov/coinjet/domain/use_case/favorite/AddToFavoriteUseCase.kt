package com.mistersomov.coinjet.domain.use_case.favorite

import com.mistersomov.coinjet.di.qualifier.DefaultDispatcher
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val repository: CoinRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(coin: Coin, currentTime: DateTime) {
        withContext(defaultDispatcher) {
            repository.addCoinToFavorite(coin, currentTime)
        }
    }
}