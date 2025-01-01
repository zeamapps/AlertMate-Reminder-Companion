package com.zeamapps.snoozy.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zeamapps.snoozy.presentation.addreminder.UpdateScreen
import com.zeamapps.snoozy.presentation.home.HomeScreen
import com.zeamapps.snoozy.presentation.onboarding.OnBoardingViewModel
import com.zeamapps.snoozy.presentation.onboarding.OnboardingScreen
import com.zeamapps.snoozy.presentation.settings.SettingsScreen
import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel


@Composable
fun ReminderAppNavigation(
    viewModel: Lazy<MainViewModel>,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel
) {
    var onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
    var reminderViewModel: ReminderViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = viewModel.value.startDestination.value
    ) {
        composable(route = ScreenRoutes.OnBoardingScreen.routes) {
            OnboardingScreen {
                onBoardingViewModel.saveEntry()
                viewModel.value.notificationPermissionGranted.value = it
            }
        }
        composable(route = ScreenRoutes.HomeScreen.routes) {
            HomeScreen(
                mainViewModel = viewModel.value,
                reminderViewModel = reminderViewModel,
                onNavigateToSettings = {
                    navController.navigate(ScreenRoutes.SettingsScreen.routes)
                },
                onNavigateToUpdate = { reminderId ->
                    Log.d("TAG", "Reminder App Navigation@#: $reminderId")
                    navController.navigate("${ScreenRoutes.UpdateScreen.routes}/$reminderId")
                }
            )
        }
        composable(route = ScreenRoutes.SettingsScreen.routes) {
            SettingsScreen(themeViewModel = themeViewModel)
        }
        composable(route = ScreenRoutes.UpdateScreen.routes +"/{reminderId}") {
            var reminderId = it.arguments?.getLong("reminderId")
            Log.d("TAG", "Reminder App Navigation: $reminderId")
            UpdateScreen(viewModel.value, FocusRequester())
        }
    }
}