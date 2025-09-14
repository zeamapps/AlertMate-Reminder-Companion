package com.zeamapps.snoozy.presentation.addreminder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.components.ColorPickerDialog
import com.zeamapps.snoozy.presentation.components.CustomDatePicker
import com.zeamapps.snoozy.presentation.components.RepeatingOptionDialog
import com.zeamapps.snoozy.presentation.components.TimePickerSample
import com.zeamapps.snoozy.utill.DateFormatHandler
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomReminder(
    mainViewModel: MainViewModel,
    onClickSave: () -> Unit,
    onClickCancel: () -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
    val showColorPicker = remember { mutableStateOf(false) }
    val showRepeatingOptions = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Reminder") },
                navigationIcon = {
                    IconButton(onClick = { onClickCancel() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (mainViewModel.reminderTittle.value.isNotEmpty()) {
                        onClickSave()
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter a title",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title Input
            OutlinedTextField(
                value = mainViewModel.reminderTittle.value,
                onValueChange = { mainViewModel.reminderTittle.value = it },
                label = { Text("Reminder Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //   Divider()

            // Selectors as simple rows
            SelectorRowSimple(
                "Date",
                DateFormatHandler().getDayFromTimestamp(mainViewModel.date.value),
                mainViewModel.tagColor.value
            ) { showDatePicker.value = true }

            SelectorRowSimple(
                "Time",
                DateFormatHandler().formatTimestampToTime(mainViewModel.time.value),
                mainViewModel.tagColor.value
            ) { showTimePicker.value = true }

            SelectorRowSimple(
                "Tag Color",
                "",
                mainViewModel.tagColor.value
            ) { showColorPicker.value = true }

            SelectorRowSimple(
                "Repeating",
                mainViewModel.repeatingOptions.value.name,
                mainViewModel.tagColor.value
            ) { showRepeatingOptions.value = true }
        }

        // Dialogs
        if (showDatePicker.value) {
            CustomDatePicker(
                onDateSelected = { if (it != null) mainViewModel.date.value = it },
                onDismiss = { showDatePicker.value = false }
            )
        }
        if (showTimePicker.value) {
            TimePickerSample(
                onConfirm = { ts ->
                    if (ts != null) mainViewModel.time.value = getTimeStamp(ts)
                    showTimePicker.value = false
                },
                onDismiss = { showTimePicker.value = false }
            )
        }
        if (showColorPicker.value) {
            ColorPickerDialog(
                onColorSelected = {
                    mainViewModel.tagColor.value = it
                    showColorPicker.value = false
                },
                onDismiss = { showColorPicker.value = false }
            )
        }
        if (showRepeatingOptions.value) {
            RepeatingOptionDialog(mainViewModel.repeatingOptions.value) {
                mainViewModel.repeatingOptions.value = it
                showRepeatingOptions.value = false
            }
        }
    }
}

@Composable
fun SelectorRowSimple(title: String, value: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value.isNotEmpty()) {
                Text(value, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
    //   Divider()
}

@Composable
fun SelectorRow(
    selectors: List<Pair<String, String?>>,
    mainViewModel: MainViewModel,
    onClick: (String) -> Unit
) {
    remember { mutableStateOf(false) }
    remember { mutableStateOf(false) }
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
                            .background(
                                color = mainViewModel.tagColor.value,
                                shape = CircleShape
                            )
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
    LocalSoftwareKeyboardController.current
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