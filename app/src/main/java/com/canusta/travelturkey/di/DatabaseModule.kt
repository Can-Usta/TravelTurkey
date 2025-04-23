package com.canusta.travelturkey.di

import android.content.Context
import androidx.room.Room
import com.canusta.travelturkey.data.local.dao.FavoriteLocationDao
import com.canusta.travelturkey.data.local.database.AppDatabase
import com.canusta.travelturkey.data.local.repository.FavoriteLocationRepository
import com.canusta.travelturkey.data.local.repository.FavoriteLocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "travel_turkey_db"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteLocationDao = db.favoriteLocationDao()

    @Provides
    fun provideFavoriteRepo(dao: FavoriteLocationDao): FavoriteLocationRepository =
        FavoriteLocationRepositoryImpl(dao)
}