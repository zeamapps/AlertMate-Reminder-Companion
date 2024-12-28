package com.zeamapps.snoozy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeamapps.snoozy.utill.SnoozyColors
import java.util.Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSample(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState: TimePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Dialog(onDismissRequest = {onDismiss()}) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp) // Extra padding around the Column to make background larger
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(12.dp), // Inner padding for content spacing
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.height(10.dp))
                    TimeInput(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(containerColor = Color.White)
                    )
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = onDismiss) {
                            Text("Dismiss", color = MaterialTheme.colorScheme.onSecondary)
                        }
                   //     Text("Cancel", fontWeight = FontWeight.Normal, color = Color.White)
                      //  Spacer(Modifier.width(20.dp))
                        Button(onClick = { onConfirm(timePickerState) }) {
                            Text("Confirm",color = MaterialTheme.colorScheme.onSecondary)
                        }
                 //       Text("Okay", fontWeight = FontWeight.Normal, color = Color.White)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialExample(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Dialog(onDismissRequest = {onDismiss()}) {
        Column {
            TimePicker(
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
}