package com.canusta.travelturkey.data.remote.repository

import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.model.CityResponse

interface CityRepository {
    suspend fun getCities(): Resource<CityResponse,RootError>
}