package com.canusta.travelturkey.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.canusta.travelturkey.R
import com.canusta.travelturkey.ui.locationmap.GpsDialogHandler
import com.canusta.travelturkey.util.getLastKnownLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch

@Composable
fun GoToMyLocationFab(cameraPositionState: CameraPositionState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showGpsDialog by remember { mutableStateOf(false) }

    if (showGpsDialog) {
        GpsDialogHandler(isGpsEnabled = false) {
            showGpsDialog = false
        }
    }

    FloatingActionButton(
        onClick = {
            val locationManager = context.getSystemService(android.location.LocationManager::class.java)
            val isGpsEnabled = locationManager?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) == true

            if (!isGpsEnabled) {
                showGpsDialog = true
                return@FloatingActionButton
            }

            val finePermissionGranted = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            val coarsePermissionGranted = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (finePermissionGranted || coarsePermissionGranted) {
                try {
                    getLastKnownLocation(context) { latLng ->
                        latLng?.let {
                            scope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
                            }
                        }
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(Icons.Filled.LocationOn, contentDescription = stringResource(R.string.go_to_location_text))
    }
}