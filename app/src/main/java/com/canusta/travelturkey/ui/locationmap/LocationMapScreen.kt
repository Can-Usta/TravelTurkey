package com.canusta.travelturkey.ui.locationmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.theme.PrimaryColor
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
    locationId: Int?,
    viewModel: LocationMapViewModel = hiltViewModel()
) {
    val location by viewModel.location.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val coordinates = remember(location?.coordinates?.lat, location?.coordinates?.lng) {
        location?.coordinates?.let { LatLng(it.lat, it.lng) }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(locationId) {
        locationId?.let(viewModel::loadLocationForMap)
    }

    LaunchedEffect(coordinates) {
        coordinates?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { location?.name?.let { Text(it) } },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            coordinates?.let {
                GoogleMap(cameraPositionState = cameraPositionState) {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Ziyaret Noktası",
                        snippet = "Buradasınız"
                    )
                }
            } ?: Text(
                text = "Geçerli konum bilgisi alınamadı.",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Show error dialog if needed
        errorMessage?.let {
            CustomErrorDialog(message = it, onDismiss = { viewModel.clearError() })
        }
    }
}