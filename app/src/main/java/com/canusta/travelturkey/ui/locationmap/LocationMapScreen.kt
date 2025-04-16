package com.canusta.travelturkey.ui.locationmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMapScreen(
    navController: NavController,
    lat: Float?,
    lng: Float?
) {
    val coordinates = remember(lat, lng) {
        if (lat != null && lng != null) LatLng(lat.toDouble(), lng.toDouble()) else null
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(coordinates) {
        coordinates?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konum Haritası") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (coordinates != null) {
            val markerState = rememberMarkerState(position = coordinates)

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    title = "Ziyaret Noktası",
                    snippet = "Buradasınız"
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Geçerli konum bilgisi alınamadı.")
            }
        }
    }
}