package com.mistersomov.coinjet.data

import com.google.gson.Gson
import com.mistersomov.coinjet.BuildConfig
import com.mistersomov.coinjet.data.database.entity.CoinEntity
import com.mistersomov.coinjet.data.database.entity.SearchCoinEntity
import com.mistersomov.coinjet.domain.model.Coin
import com.mistersomov.coinjet.data.network.model.CoinListDto
import com.mistersomov.coinjet.data.network.model.QuoteDto
import com.mistersomov.coinjet.data.network.model.QuoteJsonContainerDto
import com.mistersomov.coinjet.utils.convertTime
import java.text.NumberFormat
import java.util.*

const val EMPTY_STRING = ""

fun CoinListDto.toSimpleCoinString() =
    this.coins?.map { it.coin?.symbol }?.joinToString(",") ?: EMPTY_STRING

fun QuoteJsonContainerDto.toQuoteList(): List<QuoteDto> {
    val result = mutableListOf<QuoteDto>()
    val json = this.json ?: return result
    val coinKeySet = json.keySet()
    coinKeySet.forEach { coinKey ->
        val currencyJson = json.getAsJsonObject(coinKey)
        val currencyKeySet = currencyJson.keySet()
        currencyKeySet.forEach { currencyKey ->
            val priceInfo = Gson().fromJson(
                currencyJson.getAsJsonObject(currencyKey),
                QuoteDto::class.java
            )
            result.add(priceInfo)
        }
    }
    return result
}

fun mapResponseToCoinList(quoteList: List<QuoteDto>, coinListDto: CoinListDto): List<Coin> {
    if (coinListDto.coins.isNullOrEmpty()) return emptyList()
    val result = mutableListOf<Coin>()

    quoteList.zip(coinListDto.coins) { quote, coin ->
        val id = coinListDto.coins.indexOf(coin).toString()
        val symbol = coin.coin?.symbol ?: EMPTY_STRING
        val fullName = coin.coin?.fullName ?: EMPTY_STRING
        val fromSymbol = quote.fromSymbol ?: EMPTY_STRING
        val toSymbol = quote.toSymbol ?: EMPTY_STRING
        val price = quote.price ?: 0.0
        val lastUpdate = quote.lastUpdate.convertTime()
        val volume24hour = quote.volume24hour ?: 0.0
        val volume24hourto = quote.volume24hourto ?: 0.0
        val open24hour = quote.open24hour ?: 0.0
        val high24hour = quote.high24hour ?: 0.0
        val low24hour = quote.low24hour ?: 0.0
        val changepct24hour = quote.changepct24hour ?: 0.0
        val changepcthour = quote.changepcthour ?: 0.0
        val mktCap = quote.mktCap ?: 0.0
        val imageUrl = BuildConfig.baseImageUrl + coin.coin?.imageUrl

        val coinEntity = Coin(
            id = id,
            symbol = symbol,
            fullName = fullName,
            fromSymbol = fromSymbol,
            toSymbol = toSymbol,
            price = price,
            lastUpdate = lastUpdate,
            volume24hour = volume24hour,
            volume24hourto = volume24hourto,
            mktCap = mktCap,
            open24hour = open24hour,
            high24hour = high24hour,
            low24hour = low24hour,
            changepct24hour = changepct24hour,
            changepcthour = changepcthour,
            imageUrl = imageUrl
        )
        result.add(coinEntity)
    }

    return result
}

fun Coin.toCoinEntity(): CoinEntity = with(this) {
    CoinEntity(
        id = id,
        symbol = symbol,
        fullName = fullName,
        fromSymbol = fromSymbol,
        toSymbol = toSymbol,
        price = price,
        lastUpdate = lastUpdate,
        volume24hour = volume24hour,
        volume24hourto = volume24hourto,
        mktCap = mktCap,
        open24hour = open24hour,
        high24hour = high24hour,
        low24hour = low24hour,
        changepct24hour = changepct24hour,
        changepcthour = changepcthour,
        imageUrl = imageUrl
    )
}

fun CoinEntity.toCoin(): Coin = with(this) {
    Coin(
        id = id,
        symbol = symbol,
        fullName = fullName,
        fromSymbol = fromSymbol,
        toSymbol = toSymbol,
        price = price,
        lastUpdate = lastUpdate,
        volume24hour = volume24hour,
        volume24hourto = volume24hourto,
        mktCap = mktCap,
        open24hour = open24hour,
        high24hour = high24hour,
        low24hour = low24hour,
        changepct24hour = changepct24hour,
        changepcthour = changepcthour,
        imageUrl = imageUrl
    )
}

fun Coin.toSearchCoinEntity(): SearchCoinEntity = with(this) {
    SearchCoinEntity(
        id = id,
        symbol = symbol,
        fullName = fullName,
        fromSymbol = fromSymbol,
        toSymbol = toSymbol,
        price = price,
        lastUpdate = lastUpdate,
        volume24hour = volume24hour,
        volume24hourto = volume24hourto,
        mktCap = mktCap,
        open24hour = open24hour,
        high24hour = high24hour,
        low24hour = low24hour,
        changepct24hour = changepct24hour,
        changepcthour = changepcthour,
        imageUrl = imageUrl
    )
}

fun SearchCoinEntity.toCoin(): Coin = with(this) {
    Coin(
        id = id,
        symbol = symbol,
        fullName = fullName,
        fromSymbol = fromSymbol,
        toSymbol = toSymbol,
        price = price,
        lastUpdate = lastUpdate,
        volume24hour = volume24hour,
        volume24hourto = volume24hourto,
        mktCap = mktCap,
        open24hour = open24hour,
        high24hour = high24hour,
        low24hour = low24hour,
        changepct24hour = changepct24hour,
        changepcthour = changepcthour,
        imageUrl = imageUrl
    )
}

fun Double.formatCurrencyToDisplay(): String {
    val decimalFormat = NumberFormat.getInstance(Locale.US).also { it.maximumFractionDigits = 6 }
    return try {
        when {
            this == 1.0 -> String.format("%.1f", this)
            else -> decimalFormat.format(this)
        }
    } catch (e: Exception) {
        throw e
    }
}

fun Double.formatBigDecimalsToDisplay(): String {
    return try {
        when {
            this >= 1000000000.0 -> String.format("%.2fB", this / 1000000000)
            this >= 1000000.0 -> String.format("%.2fM", this / 1000000)
            this >=1000.0 -> String.format("%.2fK", this / 1000)
            else -> this.toString()
        }
    } catch (e: Exception) {
        throw e
    }
}