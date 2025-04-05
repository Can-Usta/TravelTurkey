package com.canusta.travelturkey.data.remote.model

data class CityResponse(
    val currentPage : Int,
    val totalPage: Int,
    val total : Int,
    val itemPerPage: Int,
    val pageSize : Int,
    val data : List<City>
)
