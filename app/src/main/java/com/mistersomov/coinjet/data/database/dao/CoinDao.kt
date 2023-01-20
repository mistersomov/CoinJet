package com.mistersomov.coinjet.data.database.dao

import androidx.room.*
import com.mistersomov.coinjet.data.database.entity.CoinInfoDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coinList: List<CoinInfoDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertByEntity(entity: CoinInfoDbModel)

    @Query("SELECT * from ${CoinInfoDbModel.TABLE_COIN_NAME} ORDER BY rank ASC")
    suspend fun getAll(): List<CoinInfoDbModel>

    @Query("SELECT * from ${CoinInfoDbModel.TABLE_COIN_NAME} WHERE id == :id LIMIT 1")
    fun getById(id: String): Flow<CoinInfoDbModel>

    @Query("DELETE from ${CoinInfoDbModel.TABLE_COIN_NAME}")
    suspend fun deleteAll()
}