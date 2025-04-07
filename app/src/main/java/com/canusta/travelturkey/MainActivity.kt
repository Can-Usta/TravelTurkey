package com.canusta.travelturkey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.canusta.travelturkey.ui.navigation.TravelTurkeyNavGraph
import com.canusta.travelturkey.ui.theme.TravelTurkeyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            false
        }
        enableEdgeToEdge()
        setContent {
            TravelTurkeyTheme {
                val navController = rememberNavController()
                TravelTurkeyNavGraph(navController)
            }
        }
    }
}