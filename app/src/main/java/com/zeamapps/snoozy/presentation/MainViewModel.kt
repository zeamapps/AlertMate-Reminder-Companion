package com.zeamapps.snoozy.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeamapps.snoozy.domain.AppEntryUseCases
import com.zeamapps.snoozy.presentation.components.RepeatingOptions
import com.zeamapps.snoozy.utill.SnoozyColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val appEntryUseCases: AppEntryUseCases
) : ViewModel() {
    var startDestination = mutableStateOf(ScreenRoutes.OnBoardingScreen.routes)
    var reminderTittle = mutableStateOf("")
        private set
    var reminderDesc = mutableStateOf("")
        private set

    var aiReminderTitle = mutableStateOf("")
        private set
    var aiRemiderDesc = mutableStateOf("")
        private set

    var notificationPermissionGranted = mutableStateOf(false)
        private set

    var tagColor = mutableStateOf(SnoozyColors.colorCodeList.random())
    var date = mutableStateOf(0L)
    var time = mutableStateOf(0L)
    var repeatingOptions = mutableStateOf(RepeatingOptions.DO_NOT_REPEAT)


    fun onReminderTitleChange(reminderTittleNewValue: String) {
        reminderTittle.value = reminderTittleNewValue
    }

    fun onReminderDescChange(reminderDescNewValue: String) {
        reminderDesc.value = reminderDescNewValue
    }

    init {
        observeAppEntry()
    }

    private fun observeAppEntry() {
        viewModelScope.launch {
            appEntryUseCases.readAppEntry()
                .collect { isAppEntryComplete ->
                    Log.d("TAG", "readEntry: $isAppEntryComplete")
                    startDestination.value = if (isAppEntryComplete) {
                        ScreenRoutes.HomeScreen.routes
                    } else {
                        ScreenRoutes.OnBoardingScreen.routes
                    }
                }
        }
    }
}