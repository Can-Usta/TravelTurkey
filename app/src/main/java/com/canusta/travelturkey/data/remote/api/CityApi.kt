package com.canusta.travelturkey.data.remote.api

import com.canusta.travelturkey.data.remote.model.CityResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CityApi {
    @GET("city-location/page-{page}.json")
    suspend fun getCities(@Path("page") page: Int): CityResponse
}