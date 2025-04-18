package com.canusta.travelturkey.ui.locationmap

import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun GpsDialogHandler(
    isGpsEnabled: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(!isGpsEnabled) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Konum Servisi Kapalı") },
            text = { Text("Harita üzerinde konumunuzu görebilmek için lütfen GPS'i açın.") },
            confirmButton = {
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    showDialog = false
                    onDismiss()
                }) {
                    Text("Ayarları Aç")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDismiss()
                }) {
                    Text("İptal")
                }
            }
        )
    }
}