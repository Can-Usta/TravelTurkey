package com.canusta.travelturkey.ui.locationmap

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.theme.PrimaryColor
import com.canusta.travelturkey.util.getLastKnownLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationMapScreen(
    navController: NavController,
    locationId: Int?,
    viewModel: LocationMapViewModel = hiltViewModel()
) {
    val location by viewModel.location.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val coarseLocationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val fineLocationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isGpsEnabled = remember { viewModel.isLocationEnabled(context) }
    var showGpsDialog by remember { mutableStateOf(!isGpsEnabled) }

    val allPermissionsGranted = fineLocationPermissionState.status.isGranted &&
            coarseLocationPermissionState.status.isGranted

    LaunchedEffect(locationId) {
        locationId?.let(viewModel::loadLocationForMap)
    }

    LaunchedEffect(Unit) {
        if (!allPermissionsGranted) {
            fineLocationPermissionState.launchPermissionRequest()
            coarseLocationPermissionState.launchPermissionRequest()
        }
    }

    val coordinates = remember(location?.coordinates?.lat, location?.coordinates?.lng) {
        location?.coordinates?.let { LatLng(it.lat, it.lng) }
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(coordinates) {
        coordinates?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    if (showGpsDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Konum Servisi Kapalı") },
            text = { Text("Harita üzerinde konumunuzu görebilmek için lütfen GPS'i açın.") },
            confirmButton = {
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    showGpsDialog = false
                }) {
                    Text("Ayarları Aç")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGpsDialog = false }) {
                    Text("İptal")
                }
            }
        )
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
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = {
                        if (!viewModel.isLocationEnabled(context)) {
                            showGpsDialog = true
                        } else {
                            getLastKnownLocation(context) { latLng ->
                                latLng?.let {
                                    scope.launch {
                                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Konumuma Git")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (allPermissionsGranted) {
                coordinates?.let {
                    GoogleMap(
                        cameraPositionState = cameraPositionState,
                        properties = com.google.maps.android.compose.MapProperties(
                            isMyLocationEnabled = true
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            myLocationButtonEnabled = false
                        )
                    ) {
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
            } else {
                Text(
                    text = "Konum izni gerekli.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            coordinates?.let {
                Button(
                    onClick = {
                        val uri = Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                ) {
                    Text("Yol Tarifi Al")
                }
            }
        }

        errorMessage?.let {
            CustomErrorDialog(message = it, onDismiss = { viewModel.clearError() })
        }
    }
}