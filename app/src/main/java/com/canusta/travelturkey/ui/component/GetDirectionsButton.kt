package com.canusta.travelturkey.ui.component

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.google.android.gms.maps.model.LatLng

@Composable
fun GetDirectionsButton(
    coordinates: LatLng,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Button(
        onClick = {
            val uri = "google.navigation:q=${coordinates.latitude},${coordinates.longitude}".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        },
        modifier = modifier
    ) {
        Text("Yol Tarifi Al")
    }
}