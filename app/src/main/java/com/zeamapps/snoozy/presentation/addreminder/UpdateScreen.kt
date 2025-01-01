package com.zeamapps.snoozy.presentation.addreminder

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.components.ColorPickerDialog
import com.zeamapps.snoozy.presentation.components.CustomDatePicker
import com.zeamapps.snoozy.presentation.components.RepeatingOptionDialog
import com.zeamapps.snoozy.presentation.components.TimePickerSample
import com.zeamapps.snoozy.utill.DateFormatHandler
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateScreen(mainViewModel: MainViewModel, focusRequester: FocusRequester){

    var showDatePicker = remember { mutableStateOf(false) }
    var showColorPicker = remember { mutableStateOf(false) }
    var showTimePicker = remember { mutableStateOf(false) }
    var showRepeatingOptions = remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Reminder Details") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }) {innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(vertical = 10.dp, horizontal = 10.dp)) {
            ReminderInputField(
                title = "Reminder Title",
                isFromAddScreen = true,
                mainViewModel,
                focusRequester
            )
            SelectorRow(
                selectors = listOf(
                    "Date" to DateFormatHandler().getDayFromTimestamp(mainViewModel.date.value),
                    "Time" to DateFormatHandler().formatTimestampToTime(mainViewModel.time.value),
                    "Tag Color" to null, // Special handling for Tag Color selector
                    "Repeating" to "Do not repeat"
                ), mainViewModel, {
                    when (it) {
                        "Date" -> showDatePicker.value = true
                        "Time" -> showTimePicker.value = true
                        "Tag Color" -> showColorPicker.value = true
                        "Repeating" -> showRepeatingOptions.value = true
                    }
                }
            )
        }

        if (showDatePicker.value) {
            CustomDatePicker({ date ->
                if (date != null) {
                    mainViewModel.date.value = date
                }
            }) { showDatePicker.value = false }
        }

        if (showTimePicker.value) {
            TimePickerSample({ timeStamp ->
                if (timeStamp != null) {
                    var timeToTimeStampValue = getTimeStamp(timeStamp)
                    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val formattedTime = formatter.format(timeToTimeStampValue)
                    Log.d("Value", "TimeStamp ->" + formattedTime + " : " + timeToTimeStampValue)
                    mainViewModel.time.value = timeToTimeStampValue
                }
                showTimePicker.value = false
            }, { showTimePicker.value = false })
        }

        if (showColorPicker.value) {
            ColorPickerDialog({
                mainViewModel.tagColor.value = it
                showColorPicker.value = false
            }, { showColorPicker.value = false })
        }

        if (showRepeatingOptions.value) {
            RepeatingOptionDialog {
                showRepeatingOptions.value = false
                mainViewModel.repeatingOptions.value = it
            }
        }
    }
}