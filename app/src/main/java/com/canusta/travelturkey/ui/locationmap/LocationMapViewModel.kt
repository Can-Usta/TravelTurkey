package com.canusta.travelturkey.ui.locationmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.model.Location
import com.canusta.travelturkey.data.remote.repository.CityRepository
import com.canusta.travelturkey.util.toLocalizedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationMapViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> get() = _location

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun loadLocationForMap(locationId: Int) {
        viewModelScope.launch {
            when (val result = repository.getLocationById(locationId)) {
                is Resource.Success -> _location.value = result.data
                is Resource.Error -> _errorMessage.value = result.error.toLocalizedMessage()
                else -> {}
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}