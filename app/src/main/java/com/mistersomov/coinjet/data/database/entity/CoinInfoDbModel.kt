package com.mistersomov.coinjet.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CoinInfoDbModel.TABLE_COIN_NAME)
data class CoinInfoDbModel(
    @PrimaryKey
    val id: String,
    val rank: Int,
    val symbol: String,
    val name: String,
    val supply: Double,
    val marketCapUsd: Double,
    val volumeUsd24Hr: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double,
    val vwap24Hr: Double,
    val imageUrl: String,
    val toSymbol: String = "USD",
) {

    companion object {
        const val TABLE_COIN_NAME = "coin_info"
    }
}