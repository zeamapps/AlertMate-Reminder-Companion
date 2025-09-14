package com.zeamapps.snoozy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zeamapps.snoozy.presentation.viewmodel.ThemeMode

import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel
import com.zeamapps.snoozy.ui.theme.MyAppTheme

import com.zeamapps.snoozy.ui.theme.SnoozyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        val viewModel = viewModels<MainViewModel>()
        val themeViewModel = viewModels<ThemeViewModel>()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SnoozyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppTheme(
                        themeViewModel.value.readTheme()
                            .collectAsState(ThemeMode.SYSTEM_DEFAULT).value
                    ) {
                        AlertMateNavigation(
                            viewModel = viewModel,
                            themeViewModel = themeViewModel.value
                        )
                    }
                }
            }
        }
    }
}