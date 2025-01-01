package com.zeamapps.snoozy.presentation

sealed class ScreenRoutes(val routes: String) {
    object SplashScreen : ScreenRoutes(routes = "splash_screen")
    object OnBoardingScreen : ScreenRoutes(routes = "onboarding_screen")
    object HomeScreen : ScreenRoutes(routes = "home_screen")
    object SettingsScreen : ScreenRoutes(routes = "settings_screen")
    object UpdateScreen : ScreenRoutes(routes = "update_Screen")
}