package com.canusta.travelturkey.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.common.Resource
import com.canusta.travelturkey.common.RootError
import com.canusta.travelturkey.data.remote.repository.CityRepository
import com.canusta.travelturkey.util.toLocalizedMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _isDataReady = MutableStateFlow(false)
    val isDataReady: StateFlow<Boolean> = _isDataReady

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchData()
    }

    fun fetchData() {
        _errorMessage.value = null
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()

            when (val result = repository.getInitialCities()) {
                is Resource.Success -> {
                    val elapsed = System.currentTimeMillis() - startTime
                    val remaining = 3000L - elapsed
                    if (remaining > 0) delay(remaining)
                    _isDataReady.value = true
                }
                is Resource.Error -> {
                    _errorMessage.value = when (val error = result.error) {
                        is RootError.Network -> error.toLocalizedMessage()
                    }
                }
            }
        }
    }
}