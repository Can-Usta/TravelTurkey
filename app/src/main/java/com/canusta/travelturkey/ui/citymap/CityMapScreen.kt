package com.canusta.travelturkey.ui.citymap

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.canusta.travelturkey.data.remote.model.Location
import com.canusta.travelturkey.ui.component.GoToMyLocationFab
import com.canusta.travelturkey.ui.locationmap.LocationPermissionHandler
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    navController: NavController,
    viewModel: CityMapViewModel = hiltViewModel()
) {
    val city by viewModel.city.collectAsState()
    val selectedIndex by viewModel.selectedLocationIndex.collectAsState()

    val locations = city?.locations ?: emptyList()
    val selectedLocation = locations.getOrNull(selectedIndex)

    val cameraPositionState = rememberCameraPositionState()
    var isMapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(isMapLoaded, selectedLocation) {
        if (isMapLoaded && selectedLocation != null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(selectedLocation.coordinates.lat, selectedLocation.coordinates.lng),
                    14f
                )
            )
        }
    }

    LocationPermissionHandler {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = city?.city ?: "Şehir") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                        }
                    }
                )
            },
            floatingActionButton = {
                GoToMyLocationFab(cameraPositionState)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = false,
                        zoomControlsEnabled = false
                    ),
                    onMapLoaded = { isMapLoaded = true }
                ) {
                    locations.forEachIndexed { index, location ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(location.coordinates.lat, location.coordinates.lng)
                            ),
                            title = location.name,
                            snippet = location.description,
                            icon = if (index == selectedIndex)
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                            else null
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                ) {
                    itemsIndexed(locations) { index, location ->
                        LocationCard(
                            location = location,
                            isSelected = index == selectedIndex,
                            onClick = {
                                viewModel.selectLocation(index)
                            },
                            onDetailClick = {
                                navController.navigate("location_detail/${location.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    location: Location,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(260.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            location.image?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = location.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onDetailClick) {
                Text("Detaya Git")
            }
        }
    }
}