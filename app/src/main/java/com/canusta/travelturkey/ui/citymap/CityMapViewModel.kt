package com.canusta.travelturkey.ui.citymap

import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.data.remote.repository.CityRepository
import com.canusta.travelturkey.util.toLocalizedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CityRepository
) : ViewModel() {

    private val _city = MutableStateFlow<City?>(null)
    val city: StateFlow<City?> = _city

    private val _selectedLocationIndex = MutableStateFlow(0)
    val selectedLocationIndex: StateFlow<Int> = _selectedLocationIndex

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        val cityIndex = savedStateHandle.get<Int>("cityIndex") ?: 0
        loadCityByIndex(cityIndex)
    }

    private fun loadCityByIndex(index: Int) {
        viewModelScope.launch {
            var currentIndex = 0
            for (page in 1..4) {
                when (val result = repository.getCitiesByPage(page)) {
                    is Resource.Success -> {
                        val cities = result.data
                        if (index < currentIndex + cities.size) {
                            _city.value = cities[index - currentIndex]
                            return@launch
                        } else {
                            currentIndex += cities.size
                        }
                    }
                    is Resource.Error -> {
                        _errorMessage.value = result.error.toLocalizedMessage()
                        return@launch
                    }
                }
            }
            _errorMessage.value = "Şehir bulunamadı"
        }
    }

    fun selectLocation(index: Int) {
        _selectedLocationIndex.value = index
    }

    fun clearErrorMessage(){
        _errorMessage.value = null
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}