package com.zeamapps.snoozy.presentation.addreminder

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.components.ColorPickerDialog
import com.zeamapps.snoozy.presentation.components.CustomDatePicker
import com.zeamapps.snoozy.presentation.components.RepeatingOption
import com.zeamapps.snoozy.presentation.components.RepeatingOptionDialog
import com.zeamapps.snoozy.presentation.components.RepeatingOptions
import com.zeamapps.snoozy.presentation.components.TimePickerSample
import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.utill.Constants
import com.zeamapps.snoozy.utill.DateFormatHandler
import com.zeamapps.snoozy.utill.SnoozyColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddReminder1(
    mainViewModel: MainViewModel,
    reminderViewModel: ReminderViewModel,
    onDismiss: () -> Unit
) {
    val isSelected = remember { mutableStateOf(true) }
    val localContext = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(SnoozyColors.SmokyBlack),
        colors = CardDefaults.cardColors(SnoozyColors.OnyxBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TitleText()
            ButtonRow(isSelected)

            if (isSelected.value) {
                CustomReminder(mainViewModel) {
                    val reminderTimeStamp = DateFormatHandler().mergeDateAndTime(
                        mainViewModel.date.value,
                        mainViewModel.time.value
                    )
                    val reminder = Reminder(
                        tittle = mainViewModel.reminderTittle.value,
                        description = mainViewModel.reminderDesc.value,
                        time = reminderTimeStamp,
                        tagColor = mainViewModel.tagColor.value.value.toLong(),
                        repeatingOptions = RepeatingOptions.DO_NOT_REPEAT
                    )
                    Log.d(
                        "Reminder",
                        "ReminderCurrentTime : " + reminderTimeStamp + " : " + mainViewModel.time.value
                    )

                    reminderViewModel.insertReminder(reminder)
                    Toast.makeText(localContext, "Reminder Added.", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            } else {
//                AddReminder(mainViewModel) {
//                    val extractedTimeStamp =
//                        DateFormatHandler().extractTimestamp(mainViewModel.aiReminderTitle.value)
//                    val timeStamp = extractedTimeStamp ?: System.currentTimeMillis()
//                    val reminder = Reminder(
//                        tittle = mainViewModel.aiReminderTitle.value,
//                        description = mainViewModel.aiRemiderDesc.value,
//                        time = timeStamp,
//                        tagColor = DateFormatHandler().convertColorToLong(mainViewModel.tagColor.value)
//                    )
//                    reminderViewModel.insertReminder(reminder)
//                    onDismiss()
//                }
            }
        }
    }
}

@Composable
fun TitleText() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = Constants.REMINDER_TITTLE,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun ButtonRow(isSelected: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Each CardButton now respects the weight within the Row
        CardButton(
            onClick = { isSelected.value = true },
            isSelected = isSelected.value,
            buttonTitle = Constants.CUSTOM,
            modifier = Modifier.weight(1f)
        )
        CardButton(
            onClick = { isSelected.value = false },
            isSelected = !isSelected.value,
            buttonTitle = Constants.WITH_AI,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CardButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    buttonTitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Black else Color.Transparent
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = buttonTitle,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminder(
    mainViewModel: MainViewModel,
    reminderViewModel: ReminderViewModel,
    onClickCancel: () -> Unit = {},
    onClickSave: () -> Unit = {},
    id: Long
) {
    val focusRequester = remember { FocusRequester() }
    val bottomSheetScaffoldState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var showDatePicker = remember { mutableStateOf(false) }
    var showColorPicker = remember { mutableStateOf(false) }
    var showTimePicker = remember { mutableStateOf(false) }
    var showRepeatingOptions = remember { mutableStateOf(false) }

    if (id != 0L) {
        var reminder = reminderViewModel.getReminderById(id)
            .collectAsState(initial = Reminder(0L, "", "", 0L, 0L, RepeatingOptions.DO_NOT_REPEAT))
        mainViewModel.reminderTittle.value = reminder.value.tittle
        mainViewModel.reminderDesc.value = reminder.value.description
        mainViewModel.tagColor.value = Color(reminder.value.tagColor)
        mainViewModel.time.value = reminder.value.time
        mainViewModel.date.value = reminder.value.time
    } else {
//        mainViewModel.reminderTittle.value = ""
//        mainViewModel.reminderDesc.value = ""
    }
    var modifier = if (id != 0L) Modifier.fillMaxHeight() else Modifier.wrapContentHeight()
    var reminderTxt = if (id == 0L) "Set Up Your Reminder" else "Reminder Details"
    ModalBottomSheet(
        onDismissRequest = onClickCancel,
        sheetState = bottomSheetScaffoldState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {},
        modifier = modifier
            .imePadding() // Adjust for the keyboard
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reminderTxt,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                )
                if (id != 0L) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp
                        ), modifier = Modifier.clickable(onClick = onClickCancel)
                    )
                }

            }


            // Input Field for Title
            // Request focus after BottomSheet animation
            LaunchedEffect(bottomSheetScaffoldState.currentValue) {
                if (bottomSheetScaffoldState.hasExpandedState) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
            }
            ReminderInputField(
                title = "Reminder Title",
                isTittle = true,
                mainViewModel,
                focusRequester
            )

            // Compact Row for Date, Time, Tag Color, and Repeating
            SelectorRow(
                selectors = listOf(
                    "Date" to DateFormatHandler().getDayFromTimestamp(mainViewModel.date.value),
                    "Time" to "3:00 PM",
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

            if (id == 0L) {
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
                            .clickable { onClickSave() },
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
            }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderInputField(
    title: String,
    isTittle: Boolean,
    mainViewModel: MainViewModel,
    focusRequester: FocusRequester
) {
    val keyboardController = LocalSoftwareKeyboardController.current


    TextField(
        value = mainViewModel.reminderTittle.value,
        onValueChange = { mainViewModel.onReminderTitleChange(it) },
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
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = if (isTittle) FontWeight.Medium else FontWeight.Medium
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
                        text = mainViewModel.repeatingOptions.value.name.replace("_"," "),
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
fun getTimeStamp(timePickerState: TimePickerState): Long {
    var calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        set(Calendar.MINUTE, timePickerState.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}
