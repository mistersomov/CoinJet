package com.mistersomov.coinjet.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mistersomov.coinjet.data.database.entity.SearchCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByEntity(entity: SearchCoinEntity)

    @Query("SELECT * from ${SearchCoinEntity.TABLE_SEARCH_COIN_NAME} ORDER BY mktCap DESC")
    fun getAll(): Flow<List<SearchCoinEntity>>

    @Query(
        "SELECT * FROM ${SearchCoinEntity.TABLE_SEARCH_COIN_NAME} " +
                "WHERE name LIKE :query OR fullName LIKE :query "
    )
    fun getAllByName(query: String): List<SearchCoinEntity>

    @Query("DELETE from ${SearchCoinEntity.TABLE_SEARCH_COIN_NAME}")
    suspend fun deleteAll()
}