package com.canusta.travelturkey.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canusta.travelturkey.data.local.entity.FavoriteLocationEntity
import com.canusta.travelturkey.data.local.repository.FavoriteLocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteLocationViewModel @Inject constructor(
    private val repository: FavoriteLocationRepository
): ViewModel() {

    val favorites: StateFlow<List<FavoriteLocationEntity>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(location: FavoriteLocationEntity, isFav: Boolean) {
        viewModelScope.launch {
            if (isFav) repository.addFavorite(location)
            else repository.removeFavorite(location)
        }
    }

    suspend fun isFavorite(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}