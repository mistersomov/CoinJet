package com.mistersomov.coinjet.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchCoinEntity.TABLE_SEARCH_COIN_NAME)
data class SearchCoinEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val fullName: String,
    val fromSymbol: String,
    val toSymbol: String,
    val price: Double,
    val lastUpdate: String,
    val volume24hour: Double,
    val volume24hourto: Double,
    val mktCap: Double,
    val open24hour: Double,
    val high24hour: Double,
    val low24hour: Double,
    val changepct24hour: Double,
    val changepcthour: Double,
    val imageUrl: String,
) {

    companion object {
        const val TABLE_SEARCH_COIN_NAME = "search_coin_entity"
    }
}