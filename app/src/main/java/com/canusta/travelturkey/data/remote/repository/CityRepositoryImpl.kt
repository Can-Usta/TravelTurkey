package com.canusta.travelturkey.data.remote.repository

import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.api.CityApi
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.data.remote.model.Location
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi
) : CityRepository {

    override suspend fun getCitiesByPage(page: Int): Resource<List<City>, RootError.Network> {
        return try {
            val response = cityApi.getCities(page)
            Resource.Success(response.data)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun getLocationById(id: Int): Resource<Location, RootError.Network> {
        return try {
            for (page in 1..4) {
                val response = cityApi.getCities(page)
                val location = response.data
                    .flatMap { it.locations }
                    .firstOrNull { it.id == id }

                if (location != null) {
                    return Resource.Success(location)
                }
            }
            Resource.Error(RootError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception): Resource.Error<Nothing, RootError.Network> {
        return when (e) {
            is HttpException -> when (e.code()) {
                400 -> Resource.Error(RootError.Network.REQUEST_TIMEOUT)
                404 -> Resource.Error(RootError.Network.SERVER_ERROR)
                429 -> Resource.Error(RootError.Network.TOO_MANY_REQUESTS)
                else -> Resource.Error(RootError.Network.UNKNOWN)
            }
            is IOException -> Resource.Error(RootError.Network.NO_INTERNET_CONNECTION)
            else -> Resource.Error(RootError.Network.UNKNOWN)
        }
    }
}