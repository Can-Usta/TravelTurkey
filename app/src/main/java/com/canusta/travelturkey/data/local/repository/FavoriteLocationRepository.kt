package com.canusta.travelturkey.data.local.repository

import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteLocationRepository {
    suspend fun addFavorite(location: FavoriteLocationEntity)
    suspend fun removeFavorite(location: FavoriteLocationEntity)
    fun getFavorites(): Flow<List<FavoriteLocationEntity>>
    suspend fun isFavorite(id: Int): Boolean
}