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
import androidx.compose.ui.res.stringResource
import com.canusta.travelturkey.R

@Composable
fun GpsDialogHandler(
    isGpsEnabled: Boolean,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(!isGpsEnabled) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(R.string.location_service_is_off_text)) },
            text = { Text(stringResource(R.string.please_turn_on_gps_to_see_your_location_on_the_map_text)) },
            confirmButton = {
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    showDialog = false
                    onDismiss()
                }) {
                    Text(stringResource(R.string.open_settings_text))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDismiss()
                }) {
                    Text(stringResource(R.string.cancel_text))
                }
            }
        )
    }
}