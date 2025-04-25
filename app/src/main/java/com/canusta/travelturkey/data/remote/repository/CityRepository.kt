package com.canusta.travelturkey.data.remote.repository

import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.data.remote.model.Location

interface CityRepository {
    suspend fun getCitiesByPage(page: Int): Resource<List<City>, RootError>
    suspend fun getLocationById(id: Int): Resource<Location, RootError>
}