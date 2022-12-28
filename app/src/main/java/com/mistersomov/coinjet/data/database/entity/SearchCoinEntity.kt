package com.mistersomov.coinjet.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchCoinEntity.TABLE_SEARCH_COIN_NAME)
data class SearchCoinEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val fullName: String,
    val fromSymbol: String,
    val toSymbol: String,
    val price: String,
    val lastUpdate: String,
    val volume24hour: String,
    val volume24hourto: String,
    val mktCap: String,
    val open24hour: String,
    val high24hour: String,
    val low24hour: String,
    val changepct24hour: String,
    val changepcthour: String,
    val imageUrl: String,
) {

    companion object {
        const val TABLE_SEARCH_COIN_NAME = "search_coin_entity"
    }
}