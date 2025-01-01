package com.zeamapps.snoozy.presentation.addreminder

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.components.ColorPickerDialog
import com.zeamapps.snoozy.presentation.components.CustomDatePicker
import com.zeamapps.snoozy.presentation.components.OutlinedTextViewDesign
import com.zeamapps.snoozy.presentation.components.RepeatingOptionDialog
import com.zeamapps.snoozy.presentation.components.StyledButton
import com.zeamapps.snoozy.presentation.components.TimePickerSample
import com.zeamapps.snoozy.utill.DateFormatHandler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomReminder(
    mainViewModel: MainViewModel,
    onclickSave: () -> Unit,
    onClickCancel: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var showDatePicker = remember { mutableStateOf(false) }
    var showColorPicker = remember { mutableStateOf(false) }
    var showTimePicker = remember { mutableStateOf(false) }
    var showRepeatingOptions = remember { mutableStateOf(false) }

    ReminderInputField(
        title = "Reminder Title",
        true,
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Cancel Button
        Column(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(2.dp))
                .clickable { onClickCancel() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }

        Spacer(modifier = Modifier.width(1.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { onclickSave() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Save",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
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

@Composable
fun SelectorRow(
    selectors: List<Pair<String, String?>>,
    mainViewModel: MainViewModel,
    onClick: (String) -> Unit
) {
    var expanded = remember { mutableStateOf(false) }
    var isAlignedToStart = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Iterate over the list of selectors and display them in a row.
        selectors.forEach { (type, name) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onClick(type) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Display the label.
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                if (type == "Tag Color") {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = mainViewModel.tagColor.value, shape = CircleShape)
                    )
                } else if (type == "Repeating") {
                    Text(
                        text = mainViewModel.repeatingOptions.value.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Medium,
                        ),
                    )

                } else {
                    Text(
                        text = name ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderInputField(
    title: String,
    isFromAddScreen: Boolean,
    mainViewModel: MainViewModel,
    focusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = if (isFromAddScreen) mainViewModel.reminderTittle.value else mainViewModel.updateReminderTitle.value,
        onValueChange = {
            if (isFromAddScreen) mainViewModel.onReminderTitleChange(it) else mainViewModel.onReminderUpdateTitleChange(
                it
            )
        },
        placeholder = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    MaterialTheme.colorScheme.onTertiary.copy(
                        0.8f
                    )
                )
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .focusRequester(focusRequester)
    )

//    LaunchedEffect(Unit) {
//        if (isTittle) {
//            focusRequester.requestFocus()
//            keyboardController?.show()
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun getTimeStamp(timePickerState: TimePickerState): Long {
    var calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        set(Calendar.MINUTE, timePickerState.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}


@Composable
fun ReminderInputFieldUI() {
    var reminderTitle = remember { mutableStateOf("") }
    var isError = remember { mutableStateOf(false) }

    EnhancedTextInputField(
        value = reminderTitle.value,
        onValueChange = {
            reminderTitle.value = it
            isError.value = it.isBlank()
        },
        label = "Reminder Title",
        placeholder = "Enter your reminder here",
        isError = isError.value,
        errorMessage = "Title cannot be empty",
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 2.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            singleLine = true
        )
        if (isError) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}