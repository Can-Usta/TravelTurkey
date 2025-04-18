package com.canusta.travelturkey.ui.locationmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun LocationMapView(
    coordinates: LatLng,
    cameraPositionState: CameraPositionState
) {
    LaunchedEffect(coordinates) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(coordinates, 15f)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {
        Marker(
            state = rememberMarkerState(position = coordinates),
            title = "Ziyaret Noktası",
            snippet = "Buradasınız"
        )
    }
}