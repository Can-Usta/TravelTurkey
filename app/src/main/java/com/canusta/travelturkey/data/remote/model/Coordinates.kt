package com.canusta.travelturkey.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Coordinates(
    val lat: Double,
    val lng: Double
) : Parcelable