package com.mistersomov.coinjet.data.database

import androidx.room.*
import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.dao.SearchCoinDao
import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel

@Database(
    entities = [CoinInfoDbModel::class, SearchCoinDbModel::class],
    version = 1,
)
abstract class CoinjetDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    abstract fun searchDao(): SearchCoinDao
}