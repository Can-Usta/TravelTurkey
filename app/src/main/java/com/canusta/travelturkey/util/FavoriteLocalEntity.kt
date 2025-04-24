package com.canusta.travelturkey.util

import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity
import com.canusta.travelturkey.data.remote.model.Location

fun Location.toFavoriteEntity(): FavoriteLocationEntity {
    return FavoriteLocationEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image,
        lat = this.coordinates.lat,
        lng = this.coordinates.lng
    )
}