package com.mistersomov.coinjet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.dao.FavoriteDao
import com.mistersomov.coinjet.data.database.dao.SearchCoinDao
import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import com.mistersomov.coinjet.data.database.entity.FavoriteDbModel
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel

@Database(
    entities = [CoinInfoDbModel::class, SearchCoinDbModel::class, FavoriteDbModel::class],
    version = 1,
)
abstract class CoinjetDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

    abstract fun searchDao(): SearchCoinDao

    abstract fun favoriteDao(): FavoriteDao
}