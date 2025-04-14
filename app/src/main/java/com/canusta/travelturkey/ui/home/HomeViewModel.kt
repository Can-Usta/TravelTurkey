package com.canusta.travelturkey.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.model.City
import com.canusta.travelturkey.data.remote.repository.CityRepository
import com.canusta.travelturkey.util.toLocalizedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getInitialCities()) {
                is Resource.Success -> _cities.value = result.data
                is Resource.Error -> {
                    _errorMessage.value = when (val error = result.error) {
                        is RootError.Network -> error.toLocalizedMessage()
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.loadNextPage()) {
                is Resource.Success -> _cities.value = result.data
                is Resource.Error -> {
                    _errorMessage.value = when (val error = result.error) {
                        is RootError.Network -> error.toLocalizedMessage()
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}