package com.canusta.travelturkey.ui.locationmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(content: @Composable () -> Unit) {
    val fineLocationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    val permissionsGranted = fineLocationPermission.status.isGranted && coarseLocationPermission.status.isGranted

    LaunchedEffect(Unit) {
        if (!permissionsGranted) {
            fineLocationPermission.launchPermissionRequest()
            coarseLocationPermission.launchPermissionRequest()
        }
    }

    if (permissionsGranted) {
        content()
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Konum izni gerekli.")
        }
    }
}