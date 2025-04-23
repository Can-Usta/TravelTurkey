package com.canusta.travelturkey.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_locations")
data class FavoriteLocationEntity(
    val name: String,
    val description: String,
    val image: String?,
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int
)