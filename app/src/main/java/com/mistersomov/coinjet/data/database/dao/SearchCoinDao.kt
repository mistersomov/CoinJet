package com.mistersomov.coinjet.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mistersomov.coinjet.data.database.entity.SearchCoinDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByEntity(entity: SearchCoinDbModel)

    @Query("SELECT * from ${SearchCoinDbModel.TABLE_SEARCH_COIN_NAME}")
    fun getAll(): Flow<List<SearchCoinDbModel>>

    @Query("DELETE from ${SearchCoinDbModel.TABLE_SEARCH_COIN_NAME}")
    suspend fun deleteAll()
}