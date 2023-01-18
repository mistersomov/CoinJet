package com.mistersomov.coinjet.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mistersomov.coinjet.data.database.entity.FavoriteDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteDbModel: FavoriteDbModel)

    @Query("SELECT * from ${FavoriteDbModel.TABLE_FAVORITE_NAME}")
    fun getAll(): Flow<List<FavoriteDbModel>>

    @Query("DELETE from ${FavoriteDbModel.TABLE_FAVORITE_NAME}")
    suspend fun deleteAll()
}