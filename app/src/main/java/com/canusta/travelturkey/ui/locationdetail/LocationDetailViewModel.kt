package com.canusta.travelturkey.ui.locationdetail

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
class LocationDetailViewModel @Inject constructor(private val repository: CityRepository) : ViewModel() {
    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadLocation(id : Int){
        viewModelScope.launch {
            when (val result = repository.getLocationById(id)) {
                is Resource.Success -> {
                    _location.value = result.data
                }
                is Resource.Error -> {
                    val error = result.error
                    if (error is RootError.Network) {
                        _errorMessage.value = error.toLocalizedMessage()
                    }
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

}