package com.canusta.travelturkey.ui.home

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
class HomeViewModel @Inject constructor(
    private val repository: CityRepository) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _expandedStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val expandedStates: StateFlow<Map<Int, Boolean>> = _expandedStates

    private var currentPage = 1
    private val maxPage = 4

    private var isInitialSet = false

    fun setInitialCities(initialCities: List<City>) {
        if (!isInitialSet) {
            _cities.value = initialCities
            _expandedStates.value = initialCities.indices.associateWith { false }
            currentPage = 1
            isInitialSet = true
        }
    }

    private fun loadPage(page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getCitiesByPage(page)) {
                is Resource.Success -> {
                    if (page == 1) {
                        _cities.value = result.data
                        _expandedStates.value = result.data.indices.associateWith { false }
                    } else {
                        val currentSize = _cities.value.size
                        val newData = result.data
                        _cities.value += newData
                        _expandedStates.value += newData.indices.associate {
                            (currentSize + it) to false
                        }
                    }
                    currentPage = page
                }
                is Resource.Error -> {
                    _errorMessage.value = result.error.toLocalizedMessage()
                }
            }
            _isLoading.value = false
        }
    }

    fun loadMore() {
        if (!_isLoading.value && currentPage < maxPage) {
            loadPage(currentPage + 1)
        }
    }

    fun toggleCard(index: Int) {
        _expandedStates.value = _expandedStates.value.toMutableMap().apply {
            this[index] = !(this[index] ?: false)
        }
    }

    fun collapseAll() {
        _expandedStates.value = _expandedStates.value.mapValues { false }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}