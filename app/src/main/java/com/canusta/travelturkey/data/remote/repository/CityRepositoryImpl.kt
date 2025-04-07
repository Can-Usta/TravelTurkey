package com.canusta.travelturkey.data.remote.repository

import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.api.CityApi
import com.canusta.travelturkey.data.remote.model.CityResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(val cityApi: CityApi) : CityRepository {
    override suspend fun getCities(): Resource<CityResponse, RootError.Network> {
        return try {
            val response = cityApi.getCities(1)
            Resource.Success(response)
        }catch (e: HttpException){
            when(e.code()){
                400 -> Resource.Error(RootError.Network.REQUEST_TIMEOUT)
                404 -> Resource.Error(RootError.Network.SERVER_ERROR)
                429 -> Resource.Error(RootError.Network.TOO_MANY_REQUESTS)
                else -> Resource.Error(RootError.Network.UNKNOWN)
            }
        }catch (e: IOException) {
            Resource.Error(RootError.Network.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            Resource.Error(RootError.Network.UNKNOWN)
        }
    }
}