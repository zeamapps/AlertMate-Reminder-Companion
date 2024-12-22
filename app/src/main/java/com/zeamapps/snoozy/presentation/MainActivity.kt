package com.zeamapps.snoozy.presentation

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zeamapps.snoozy.presentation.ScreenRoutes.OnBoardingScreen
import com.zeamapps.snoozy.presentation.onboarding.OnboardingScreen
import com.zeamapps.snoozy.presentation.settings.SettingsScreen
import com.zeamapps.snoozy.presentation.viewmodel.ThemeMode

import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel
import com.zeamapps.snoozy.ui.theme.MyAppTheme

import com.zeamapps.snoozy.ui.theme.SnoozyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val viewModel = viewModels<MainViewModel>()
        val themeViewModel = viewModels<ThemeViewModel>()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        splashScreen.setKeepOnScreenCondition{
//          //  viewModel.value.startDestination.value == ScreenRoutes.OnBoardingScreen.routes
//        }
        setContent {
            SnoozyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppTheme(themeViewModel.value.readTheme().collectAsState(ThemeMode.SYSTEM_DEFAULT).value) {
                       ReminderAppNavigation(viewModel = viewModel, themeViewModel = themeViewModel.value)

                       //
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier, Color.DarkGray
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SnoozyTheme {
        Greeting("Android")
    }
}



