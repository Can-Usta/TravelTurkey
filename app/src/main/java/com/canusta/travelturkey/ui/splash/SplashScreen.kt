package com.canusta.travelturkey.ui.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.canusta.travelturkey.R
import com.canusta.travelturkey.ui.component.RetryDialog
import com.canusta.travelturkey.ui.navigation.NavRoot

@Composable
fun TravelTurkeySplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val isDataReady by viewModel.isDataReady.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val initialCities by viewModel.initialCities.collectAsState()
    val counter = remember { mutableStateOf(0) }

    LaunchedEffect(isDataReady) {
        if (isDataReady) {
            navController.currentBackStackEntry?.savedStateHandle?.set("initialCities", initialCities)

            navController.navigate(NavRoot.HOME.route) {
                popUpTo(NavRoot.SPLASH.route) { inclusive = true }
                launchSingleTop = true
                restoreState = true
            }
            counter.value += 1
            Log.d("TravelTurkeySplashScreen", "Navigated to Home $counter ")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.travel_turkey_icon),
                contentDescription = "App Icon",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isDataReady && errorMessage == null) {
                CircularProgressIndicator()
            }

            if (errorMessage != null) {
                RetryDialog(
                    errorMessage = errorMessage ?: "",
                    onRetry = { viewModel.fetchData() }
                )
            }
        }
    }
}