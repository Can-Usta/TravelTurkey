package com.canusta.travelturkey.ui.navigation

enum class NavRoot(val route: String) {
    SPLASH("splash"),
    HOME("home"),
    LOCATION_DETAIL("location_detail/{locationId}"),
    FAVORITE("favorite"),
    CITY_MAP("city_map"),
    LOCATION_MAP("location_map/{lat}/{lng}")
}