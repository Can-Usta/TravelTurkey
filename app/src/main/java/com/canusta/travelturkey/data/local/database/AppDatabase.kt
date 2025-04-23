package com.canusta.travelturkey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.canusta.travelturkey.data.local.dao.FavoriteLocationDao
import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity

@Database(entities = [FavoriteLocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteLocationDao(): FavoriteLocationDao
}