package com.canusta.travelturkey.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val city: String,
    val locations: List<Location>
) : Parcelable
