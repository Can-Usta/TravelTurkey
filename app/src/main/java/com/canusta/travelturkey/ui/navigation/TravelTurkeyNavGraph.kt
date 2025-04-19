package com.canusta.travelturkey.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.canusta.travelturkey.ui.citymap.CityMapScreen
import com.canusta.travelturkey.ui.home.HomeScreen
import com.canusta.travelturkey.ui.locationdetail.LocationDetailScreen
import com.canusta.travelturkey.ui.locationmap.LocationMapScreen
import com.canusta.travelturkey.ui.splash.TravelTurkeySplashScreen

@Composable
fun TravelTurkeyNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = NavRoot.SPLASH.route) {
        composable(NavRoot.SPLASH.route) {
            TravelTurkeySplashScreen(navController)
        }
        composable(NavRoot.HOME.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = NavRoot.LOCATION_DETAIL.route,
            arguments = listOf(navArgument("locationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getInt("locationId")
            LocationDetailScreen(locationId = locationId, navController)
        }
        composable(
            route = NavRoot.LOCATION_MAP.route,
            arguments = listOf(navArgument("locationId"){type = NavType.IntType})
        ) {backStackEntry ->
            val locationId = backStackEntry.arguments?.getInt("locationId")
            LocationMapScreen(
                navController = navController,
                locationId = locationId
            )
        }
        composable(
            route = "city_map/{cityIndex}",
            arguments = listOf(navArgument("cityIndex") { type = NavType.IntType })
        ) {
            CityMapScreen(navController = navController)
        }
    }
}