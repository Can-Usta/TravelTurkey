package com.canusta.travelturkey.ui.citymap

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.canusta.travelturkey.R
import com.canusta.travelturkey.data.remote.model.Location
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.component.GoToMyLocationFab
import com.canusta.travelturkey.ui.locationmap.GpsDialogHandler
import com.canusta.travelturkey.ui.locationmap.LocationPermissionHandler
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    navController: NavController,
    viewModel: CityMapViewModel = hiltViewModel()
) {
    val city by viewModel.city.collectAsState()
    val selectedIndex by viewModel.selectedLocationIndex.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val locations = city?.locations ?: emptyList()
    val selectedLocation = locations.getOrNull(selectedIndex)
    val context = LocalContext.current


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
    GpsDialogHandler(isGpsEnabled = viewModel.isLocationEnabled(context))
    LocationPermissionHandler {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = city?.city ?: stringResource(R.string.city_text)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                                R.string.back_text
                            )
                            )
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
                if(errorMessage != null) {
                    CustomErrorDialog(errorMessage.toString()) {
                        viewModel.clearErrorMessage()
                    }
                }
                else{
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
                                onClick = { viewModel.selectLocation(index) },
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
}

@Composable
fun LocationCard(
    location: Location,
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

            if(location.image != null){
                Image(
                    painter = rememberAsyncImagePainter(location.image),
                    contentDescription = location.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }else{
                Image(
                    painter = painterResource(R.drawable.no_image_found),
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
                Text(stringResource(R.string.go_to_detail_text))
            }
        }
    }
}