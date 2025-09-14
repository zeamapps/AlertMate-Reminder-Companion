package com.zeamapps.snoozy.presentation

sealed class ScreenRoutes(val route: String) {
    object OnBoardingScreen : ScreenRoutes(route = "onboarding_screen")
    object HomeScreen : ScreenRoutes(route = "home_screen")
    object SettingsScreen : ScreenRoutes(route = "settings_screen")
    object UpdateScreen : ScreenRoutes(route = "update_Screen")
    object AddReminderScreen : ScreenRoutes(route = "add_reminder_screen")
}