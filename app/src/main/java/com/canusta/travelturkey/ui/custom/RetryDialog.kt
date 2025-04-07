package com.canusta.travelturkey.ui.custom

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.canusta.travelturkey.R

@Composable
fun RetryDialog(
    errorMessage: String,
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Dialog kapatılamasın
        confirmButton = {
            Button(onClick = onRetry) {
                Text(stringResource(R.string.try_again))
            }
        },
        title = { Text(stringResource(R.string.error)) },
        text = { Text(errorMessage) }
    )
}