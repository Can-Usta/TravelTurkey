package com.canusta.travelturkey.util

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getLastKnownLocation(context: Context, onLocationResult: (LatLng?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(LatLng(location.latitude, location.longitude))
        } else {
            onLocationResult(null)
        }
    }.addOnFailureListener {
        onLocationResult(null)
    }
}