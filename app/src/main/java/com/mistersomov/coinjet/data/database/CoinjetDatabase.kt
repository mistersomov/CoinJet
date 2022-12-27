package com.mistersomov.coinjet.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mistersomov.coinjet.data.database.dao.CoinDao
import com.mistersomov.coinjet.data.database.entity.CoinEntity

@Database(entities = [CoinEntity::class], version = 1)
abstract class CoinjetDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao

}