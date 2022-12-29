package com.mistersomov.coinjet.data

import com.google.gson.Gson
import com.mistersomov.coinjet.BuildConfig
import com.mistersomov.coinjet.data.database.entity.CoinEntity
import com.mistersomov.coinjet.data.database.entity.SearchCoinEntity
import com.mistersomov.coinjet.data.model.Coin
import com.mistersomov.coinjet.data.network.model.CoinListDto
import com.mistersomov.coinjet.data.network.model.QuoteDto
import com.mistersomov.coinjet.data.network.model.QuoteJsonContainerDto
import com.mistersomov.coinjet.utils.convertTime
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

const val EMPTY_STRING = ""

fun CoinListDto.toSimpleCoinString() =
    this.coins?.map { it.coin?.name }?.joinToString(",") ?: EMPTY_STRING

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
        val id = coin.coin?.id ?: EMPTY_STRING
        val name = coin.coin?.name ?: EMPTY_STRING
        val fullName = coin.coin?.fullName ?: EMPTY_STRING
        val fromSymbol = quote.fromSymbol ?: EMPTY_STRING
        val toSymbol = quote.toSymbol ?: EMPTY_STRING
        val price = formatPrice(quote.price)
        val lastUpdate = quote.lastUpdate.convertTime()
        val volume24hour = quote.volume24hour?.toString() ?: EMPTY_STRING
        val volume24hourto = quote.volume24hourto?.toString() ?: EMPTY_STRING
        val open24hour = quote.open24hour?.toString() ?: EMPTY_STRING
        val high24hour = quote.high24hour?.toString() ?: EMPTY_STRING
        val low24hour = quote.low24hour?.toString() ?: EMPTY_STRING
        val changepct24hour = quote.changepct24hour?.toString() ?: EMPTY_STRING
        val changepcthour = quote.changepcthour?.toString() ?: EMPTY_STRING
        val mktCap = quote.mktCap?.toString() ?: EMPTY_STRING
        val imageUrl = BuildConfig.baseImageUrl + coin.coin?.imageUrl

        val coinEntity = Coin(
            id = id,
            name = name,
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
        name = name,
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
        name = name,
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
        name = name,
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
        name = name,
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

private fun formatPrice(price: Double?): String {
    val symbols = DecimalFormatSymbols(Locale.ENGLISH)
    if (price != null) {
        if (price < 1.0) {
            return DecimalFormat("#.######", symbols).format(price)
        }
        else if (price == 1.0) {
            return DecimalFormat("#.0", symbols).format(price)
        }
    }
    return DecimalFormat("###.######", symbols).format(price)
}