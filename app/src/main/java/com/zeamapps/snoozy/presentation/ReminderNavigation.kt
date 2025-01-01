package com.zeamapps.snoozy.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch


@Composable
fun ReminderAppNavigation(
    viewModel: Lazy<MainViewModel>,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel
) {
    val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
    val reminderViewModel: ReminderViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = viewModel.value.startDestination.value
    ) {
        // Onboarding Screen
        composable(route = ScreenRoutes.OnBoardingScreen.routes) {
            OnboardingScreen { notificationGranted ->
                onBoardingViewModel.saveEntry()
                viewModel.value.notificationPermissionGranted.value = notificationGranted
            }
        }

        // Home Screen
        composable(route = ScreenRoutes.HomeScreen.routes) {
            HomeScreen(
                mainViewModel = viewModel.value,
                reminderViewModel = reminderViewModel,
                onNavigateToSettings = {
                    navController.navigate(ScreenRoutes.SettingsScreen.routes)
                },
                onNavigateToUpdate = { reminderId ->
                    Log.d("TAG", "Navigating to UpdateScreen with reminderId: $reminderId")
                    navController.navigate("${ScreenRoutes.UpdateScreen.routes}/$reminderId")
                }
            )
        }

        // Settings Screen
        composable(route = ScreenRoutes.SettingsScreen.routes) {
            SettingsScreen(themeViewModel = themeViewModel)
        }

        // Update Screen
        composable(route = "${ScreenRoutes.UpdateScreen.routes}/{reminderId}") { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toLongOrNull()
            val reminder = reminderViewModel.getReminderById(reminderId!!).collectAsState(initial = null).value

            UpdateScreen(viewModel.value, FocusRequester(), reminderId, reminderViewModel)

            BackHandler {
                backStackEntry.lifecycleScope.launch {
                    reminderViewModel.updateReminder(
                        reminder!!.copy(
                            id = reminderId,
                            tittle = viewModel.value.updateReminderTitle.value,
                            description = viewModel.value.reminderDesc.value,
                            time = viewModel.value.time.value,
                            tagColor = viewModel.value.tagColor.value.value.toLong(),
                            repeatingOptions = viewModel.value.repeatingOptions.value
                        )
                    )
                    navController.popBackStack()
                }
            }
        }
    }
}