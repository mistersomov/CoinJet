package com.mistersomov.coinjet.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavoriteDbModel.TABLE_FAVORITE_NAME)
data class FavoriteDbModel(
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
    val toSymbol: String,
    val time: Long,
) {

    companion object {
        const val TABLE_FAVORITE_NAME = "favorite_entity"
    }
}