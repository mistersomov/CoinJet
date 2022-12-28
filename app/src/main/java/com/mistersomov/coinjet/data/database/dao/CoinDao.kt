package com.mistersomov.coinjet.data.database.dao

import androidx.room.*
import com.mistersomov.coinjet.data.database.entity.CoinEntity
import com.mistersomov.coinjet.data.database.entity.SearchCoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coinList: List<CoinEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByEntity(entity: CoinEntity)

    @Query("SELECT * from ${CoinEntity.TABLE_COIN_NAME} ORDER BY mktCap DESC")
    fun getAll(): Flow<List<CoinEntity>>

    @Query("SELECT * from ${CoinEntity.TABLE_COIN_NAME} WHERE id == :id LIMIT 1")
    fun getById(id: String): Flow<CoinEntity>

    @Query(
        "SELECT * FROM ${CoinEntity.TABLE_COIN_NAME} " +
                "WHERE name LIKE :query OR fullName LIKE :query "
    )
    suspend fun getAllByName(query: String): List<CoinEntity>

    @Query("DELETE from ${CoinEntity.TABLE_COIN_NAME}")
    suspend fun deleteAll()
}