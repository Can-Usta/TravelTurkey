package com.canusta.travelturkey.ui.citymap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.data.remote.repository.CityRepository
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

    init {
        val cityIndex = savedStateHandle.get<Int>("cityIndex") ?: 0
        viewModelScope.launch {
            when (val result = repository.getInitialCities()) {
                is Resource.Success -> {
                    _city.value = result.data.getOrNull(cityIndex)
                }
                else -> {
                    _city.value = null // Hata durumu için fallback
                }
            }
        }
    }

    fun selectLocation(index: Int) {
        _selectedLocationIndex.value = index
    }
}