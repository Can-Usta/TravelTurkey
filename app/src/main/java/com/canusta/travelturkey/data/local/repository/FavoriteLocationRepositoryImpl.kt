package com.canusta.travelturkey.data.local.repository

import com.canusta.travelturkey.data.local.dao.FavoriteLocationDao
import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteLocationRepositoryImpl @Inject constructor(
    private val dao: FavoriteLocationDao
) : FavoriteLocationRepository {

    override suspend fun addFavorite(location: FavoriteLocationEntity) = dao.insert(location)

    override suspend fun removeFavorite(location: FavoriteLocationEntity) = dao.delete(location)

    override fun getFavorites(): Flow<List<FavoriteLocationEntity>> = dao.getAllFavorites()

    override suspend fun isFavorite(id: Int): Boolean = dao.isFavorite(id)
}