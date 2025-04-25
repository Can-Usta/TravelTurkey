package com.canusta.travelturkey.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val name: String,
    val description: String,
    val coordinates: Coordinates,
    val image: String?,
    val id: Int
):Parcelable

