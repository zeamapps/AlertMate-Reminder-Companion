package com.zeamapps.snoozy.presentation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.presentation.addreminder.CustomReminder
import com.zeamapps.snoozy.presentation.addreminder.UpdateScreen
import com.zeamapps.snoozy.presentation.home.HomeScreen
import com.zeamapps.snoozy.presentation.onboarding.OnBoardingViewModel
import com.zeamapps.snoozy.presentation.onboarding.OnboardingScreen
import com.zeamapps.snoozy.presentation.settings.SettingsScreen
import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel
import com.zeamapps.snoozy.utill.DateFormatHandler
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun AlertMateNavigation(
    viewModel: Lazy<MainViewModel>,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel
) {
    val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
    val reminderViewModel: ReminderViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = viewModel.value.startDestination.value
    ) {
        // Onboarding Screen
        composable(route = ScreenRoutes.OnBoardingScreen.route) {
            OnboardingScreen { notificationGranted ->
                onBoardingViewModel.saveEntry()
                viewModel.value.notificationPermissionGranted.value = notificationGranted
            }
        }

        // Home Screen
        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(
                mainViewModel = viewModel.value,
                reminderViewModel = reminderViewModel,
                onNavigateToSettings = {
                    navController.navigate(ScreenRoutes.SettingsScreen.route)
                },
                onNavigateToUpdate = { reminderId ->
                    Log.d("TAG", "Navigating to UpdateScreen with reminderId: $reminderId")
                    navController.navigate("${ScreenRoutes.UpdateScreen.route}/$reminderId")
                },
                onNavigateToAddReminder = {
                    navController.navigate(ScreenRoutes.AddReminderScreen.route)
                }
            )
        }

        // Settings Screen
        composable(route = ScreenRoutes.SettingsScreen.route) {
            SettingsScreen(themeViewModel = themeViewModel)
        }

        // Update Screen
        composable(route = "${ScreenRoutes.UpdateScreen.route}/{reminderId}") { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toLongOrNull()
            val reminder =
                reminderViewModel.getReminderById(reminderId!!).collectAsState(initial = null).value

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

        composable(route = ScreenRoutes.AddReminderScreen.route) {
            CustomReminder(mainViewModel = viewModel.value, onClickCancel = {
                navController.popBackStack()
            }, onClickSave = {
                val mainViewModel = viewModel.value
                var mergedTimeStamp = DateFormatHandler().mergeDateAndTime(
                    mainViewModel.date.value,
                    mainViewModel.time.value
                )

                if (mergedTimeStamp >= System.currentTimeMillis()) {
                    var reminderTitle = mainViewModel.reminderTittle.value

                    var reminderTimestamp = mergedTimeStamp

                    val reminder = Reminder(
                        tittle = reminderTitle,
                        description = mainViewModel.reminderDesc.value,
                        time = reminderTimestamp!!,
                        tagColor = mainViewModel.tagColor.value.value.toLong(),
                        repeatingOptions = mainViewModel.repeatingOptions.value
                    )
                    reminderViewModel.insertReminder(reminder)
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Reminder time cannot be in the past!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}