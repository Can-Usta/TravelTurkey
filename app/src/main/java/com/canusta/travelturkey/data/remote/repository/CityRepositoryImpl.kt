package com.canusta.travelturkey.data.remote.repository

import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.api.CityApi
import com.canusta.travelturkey.data.remote.model.City
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityApi: CityApi
) : CityRepository {

    private val _cachedCities = mutableListOf<City>()
    private var totalPage = 1
    private var currentPage = 1

    override suspend fun getInitialCities(): Resource<List<City>, RootError.Network> {
        if (_cachedCities.isNotEmpty()) {
            return Resource.Success(_cachedCities.toList())
        }

        return try {
            val response = cityApi.getCities(1)
            _cachedCities.clear()
            _cachedCities.addAll(response.data)
            totalPage = response.totalPage
            currentPage = 1
            Resource.Success(_cachedCities.toList())
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun loadNextPage(): Resource<List<City>, RootError.Network> {
        if (currentPage >= totalPage) {
            return Resource.Success(_cachedCities.toList())
        }

        return try {
            val nextPage = currentPage + 1
            val response = cityApi.getCities(nextPage)
            _cachedCities.addAll(response.data)
            currentPage = nextPage
            Resource.Success(_cachedCities.toList())
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