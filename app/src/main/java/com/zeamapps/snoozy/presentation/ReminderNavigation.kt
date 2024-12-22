package com.zeamapps.snoozy.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zeamapps.snoozy.notification.FullScreenNotificationPrompt
import com.zeamapps.snoozy.presentation.home.HomeScreen
import com.zeamapps.snoozy.presentation.onboarding.OnBoardingViewModel
import com.zeamapps.snoozy.presentation.onboarding.OnboardingScreen
import com.zeamapps.snoozy.presentation.settings.SettingsScreen
import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel


@Composable
fun ReminderAppNavigation(viewModel: Lazy<MainViewModel>, navController: NavHostController = rememberNavController(), themeViewModel: ThemeViewModel) {
    var onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
    var reminderViewModel: ReminderViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination =  viewModel.value.startDestination.value){
        composable(route = ScreenRoutes.OnBoardingScreen.routes){
           OnboardingScreen {
               onBoardingViewModel.saveEntry()
               viewModel.value.notificationPermissionGranted.value = it
           }
        }
        composable(route = ScreenRoutes.HomeScreen.routes){
            HomeScreen(viewModel.value, reminderViewModel){
                navController.navigate(ScreenRoutes.SettingsScreen.routes)
            }
        }
        composable(route= ScreenRoutes.SettingsScreen.routes){
            SettingsScreen(themeViewModel = themeViewModel )
        }
    }
}