package com.canusta.travelturkey.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: FavoriteLocationEntity)

    @Delete
    suspend fun delete(location: FavoriteLocationEntity)

    @Query("SELECT * FROM favorite_locations")
    fun getAllFavorites(): Flow<List<FavoriteLocationEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_locations WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}