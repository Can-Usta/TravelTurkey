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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.R
import com.canusta.travelturkey.ui.component.CustomErrorDialog
import com.canusta.travelturkey.ui.component.GetDirectionsButton
import com.canusta.travelturkey.ui.component.GoToMyLocationFab
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationMapScreen(
    navController: NavController,
    locationId: Int?,
    viewModel: LocationMapViewModel = hiltViewModel()
) {
    val location by viewModel.location.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()

    val coordinates = remember(location?.coordinates?.lat, location?.coordinates?.lng) {
        location?.coordinates?.let { LatLng(it.lat, it.lng) }
    }

    LaunchedEffect(locationId) {
        locationId?.let(viewModel::loadLocationForMap)
    }

    GpsDialogHandler(isGpsEnabled = viewModel.isLocationEnabled(context))

    Scaffold(
        topBar = {
            LocationTopBar(title = location?.name ?: "", onBack = { navController.popBackStack() })
        },
        floatingActionButton = {
            GoToMyLocationFab(cameraPositionState = cameraPositionState)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LocationPermissionHandler {
                coordinates?.let {
                    LocationMapView(
                        coordinates = it,
                        cameraPositionState = cameraPositionState
                    )
                    GetDirectionsButton(
                        coordinates = it,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    )
                } ?: Text(
                    text = stringResource(R.string.unable_to_obtain_valid_location_information_text),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMessage?.let {
                CustomErrorDialog(message = it, onDismiss = { viewModel.clearError() })
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
            }
        }
    )
}