package com.zeamapps.snoozy.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.zeamapps.snoozy.R
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.utill.DateFormatHandler
import com.zeamapps.snoozy.utill.SnoozyColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextViewDesign(mainViewModel: MainViewModel) {

    var timeStamp = remember { mutableStateOf(0L) }
    var showDatePicker = remember { mutableStateOf(false) }
    var showTimePicker = remember { mutableStateOf(false) }
    var showColorPicker = remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
    Column(Modifier.wrapContentSize()) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 2.dp, start = 20.dp, end = 20.dp),
            colors = CardDefaults.cardColors(SnoozyColors.SmokyBlack.copy(alpha = 0.5f)),
            shape = shape
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                WishTextField(
                    label = "Reminder",
                    onValueChange = { mainViewModel.onReminderTitleChange(it) },
                    value = mainViewModel.reminderTittle.value
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .width(110.dp)
                    .height(40.dp)
                    .padding(top = 6.dp, start = 20.dp, end = 4.dp)
                    .clickable { showColorPicker.value = true },
                colors = CardDefaults.cardColors(SnoozyColors.SmokyBlack.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.size(12.dp),
                        shape = RoundedCornerShape(32),
                        colors = CardDefaults.cardColors(
                            mainViewModel.tagColor.value
                        )
                    ) { }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Color",
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp)
                    .padding(top = 6.dp, start = 4.dp, end = 4.dp)
                    .clickable { showTimePicker.value = true },
                colors = CardDefaults.cardColors(SnoozyColors.SmokyBlack.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.clock_icon), // Replace with your drawable's name
                        contentDescription = "Example Image", // Accessibility description
                        modifier = Modifier.size(20.dp), // Adjust size as needed
                    )
                    Spacer(Modifier.width(6.dp))
                    var timestampValue =
                        if (timeStamp.value == 0L) "3.00 pm" else DateFormatHandler().formatTimestampToTime(
                            timeStamp.value
                        )
                    Text(timestampValue, fontWeight = FontWeight.Light, color = Color.White)
                }
            }

            Card(
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp)
                    .padding(top = 6.dp, start = 4.dp, end = 4.dp)
                    .clickable {
                        showDatePicker.value = true
                    },
                colors = CardDefaults.cardColors(SnoozyColors.SmokyBlack.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.calendar_icon), // Replace with your drawable's name
                        contentDescription = "Example Image", // Accessibility description
                        modifier = Modifier.size(20.dp), // Adjust size as needed
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        DateFormatHandler().getDayFromTimestamp(mainViewModel.date.value),
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                }
            }
        }
    }
    fun getTimeStamp(timePickerState: TimePickerState): Long {
        var calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            set(Calendar.MINUTE, timePickerState.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
    if (showDatePicker.value) {
        CustomDatePicker({ date ->
            if (date != null) {
                mainViewModel.date.value = date
            }
        }, { showDatePicker.value = false })
    }
    if (showTimePicker.value) {
        TimePickerSample({ time ->

            if (time != null) {
                var timeToTimeStampValue = getTimeStamp(time)
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedTime = formatter.format(timeToTimeStampValue)
                Log.d("Value", "TimeStamp ->" + formattedTime + " : " + timeToTimeStampValue)
                mainViewModel.time.value = timeToTimeStampValue
                //  timeStamp.value = timeToTimeStampValue
            }
            showTimePicker.value = false
        }, { showTimePicker.value = false })

    }

    if (showColorPicker.value) {
        ColorPickerDialog({
            mainViewModel.tagColor.value = it
            showColorPicker.value = false
            showDatePicker.value = false
        }, { showColorPicker.value = false })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishTextField(label: String = "", value: String = "", onValueChange: (String) -> Unit = { }) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputExample(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column {
        TimeInput(
            state = timePickerState,
        )
        Button(onClick = onDismiss) {
            Text("Dismiss picker")
        }
        Button(onClick = onConfirm) {
            Text("Confirm selection")
        }
    }
}


