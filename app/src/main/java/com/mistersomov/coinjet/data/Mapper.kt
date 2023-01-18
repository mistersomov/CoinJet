package com.mistersomov.coinjet.data

import com.mistersomov.coinjet.BuildConfig
import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel
import com.mistersomov.coinjet.data.network.model.CoinInfoDto
import com.mistersomov.coinjet.domain.model.Coin
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

fun CoinInfoDto.toDbModel(): CoinInfoDbModel {
    val id: String = checkNotNull(this.id)
    val rank: Int = checkNotNull(this.rank).toInt()
    val symbol: String = checkNotNull(this.symbol)
    val name: String = checkNotNull(this.name)
    val supply: Double = checkNotNull(this.supply).toDouble()
    val marketCapUsd = checkNotNull(this.marketCapUsd).toDouble()
    val volumeUsd24Hr = checkNotNull(this.volumeUsd24Hr).toDouble()
    val priceUsd = checkNotNull(this.priceUsd).toDouble()
    val changePercent24Hr = checkNotNull(this.changePercent24Hr).toDouble()
    val vwap24Hr = 0.0
    val imageUrl = BuildConfig.baseImageUrl + "/assets/icons/" + symbol.lowercase() + "@2x.png"

    return CoinInfoDbModel(
        id = id,
        rank = rank,
        symbol = symbol,
        name = name,
        supply = supply,
        marketCapUsd = marketCapUsd,
        volumeUsd24Hr = volumeUsd24Hr,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        vwap24Hr = vwap24Hr,
        imageUrl = imageUrl,
    )
}

fun CoinInfoDbModel.toEntity(): Coin = Coin(
    id = this.id,
    rank = this.rank,
    symbol = this.symbol,
    name = this.name,
    supply = this.supply,
    marketCapUsd = this.marketCapUsd,
    volumeUsd24Hr = this.volumeUsd24Hr,
    priceUsd = this.priceUsd,
    changePercent24Hr = this.changePercent24Hr,
    vwap24Hr = this.vwap24Hr,
    imageUrl = this.imageUrl,
    toSymbol = this.toSymbol,
)

fun Coin.toSearchCoinDbModel(time: DateTime): SearchCoinDbModel = SearchCoinDbModel(
    id = this.id,
    rank = this.rank,
    symbol = this.symbol,
    name = this.name,
    supply = this.supply,
    marketCapUsd = this.marketCapUsd,
    volumeUsd24Hr = this.volumeUsd24Hr,
    priceUsd = this.priceUsd,
    changePercent24Hr = this.changePercent24Hr,
    vwap24Hr = this.vwap24Hr,
    imageUrl = this.imageUrl,
    toSymbol = this.toSymbol,
    time = time.millis,
)

fun SearchCoinDbModel.toEntity(): Coin = Coin(
    id = this.id,
    rank = this.rank,
    symbol = this.symbol,
    name = this.name,
    supply = this.supply,
    marketCapUsd = this.marketCapUsd,
    volumeUsd24Hr = this.volumeUsd24Hr,
    priceUsd = this.priceUsd,
    changePercent24Hr = this.changePercent24Hr,
    vwap24Hr = this.vwap24Hr,
    imageUrl = this.imageUrl,
    toSymbol = this.toSymbol,
)