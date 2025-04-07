package com.canusta.travelturkey.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canusta.travelturkey.ui.home.HomeScreen
import com.canusta.travelturkey.ui.splash.TravelTurkeySplashScreen

@Composable
fun TravelTurkeyNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = NavRoot.SPLASH.route) {
        composable(NavRoot.SPLASH.route) {
            TravelTurkeySplashScreen(navController)
        }
        composable(NavRoot.HOME.route) {
            HomeScreen()
        }
    }
}