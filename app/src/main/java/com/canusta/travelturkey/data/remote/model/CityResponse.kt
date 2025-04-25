package com.canusta.travelturkey.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityResponse(
    val currentPage : Int,
    val totalPage: Int,
    val total : Int,
    val itemPerPage: Int,
    val pageSize : Int,
    val data : List<City>
):Parcelable