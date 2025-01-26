package com.zeamapps.snoozy.presentation.home

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.notification.getNotificationPermissionState
import com.zeamapps.snoozy.notification.openNotificationSettings
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.addreminder.AddReminder
import com.zeamapps.snoozy.presentation.components.AppBar
import com.zeamapps.snoozy.presentation.components.DateCard
import com.zeamapps.snoozy.presentation.components.NotificationSettingInline
import com.zeamapps.snoozy.presentation.components.PulsedFloatingActionBtn
import com.zeamapps.snoozy.presentation.components.generateDatesFromCurrentDate
import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.utill.Constants
import com.zeamapps.snoozy.utill.DateFormatHandler
import java.sql.Timestamp
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    reminderViewModel: ReminderViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToUpdate: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }
    val showUpdateReminder = remember { mutableStateOf(false) }
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember(currentMonth) { generateDatesFromCurrentDate() }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val localDateTime = selectedDate.value.atStartOfDay()
    val timestamp = Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    val reminderId = remember { mutableStateOf(0L) }
    val reminders = reminderViewModel.reminderList.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val hasPermission = getNotificationPermissionState(context).collectAsState()
    mainViewModel.date.value = timestamp.time


    Scaffold(
        floatingActionButton = {
            PulsedFloatingActionBtn { showBottomSheet.value = true }
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppBar(Constants.APP_NAME) { onNavigateToSettings() }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Horizontal Date Selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daysInMonth) { date ->
                    DateCard(
                        date = date,
                        isSelected = date == selectedDate.value,
                        onClick = { selectedDate.value = date }
                    )
                }
            }

            // Notification Permission Inline Message
            if (!hasPermission.value) {
                NotificationSettingInline {
                    openNotificationSettings(context)
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Section Title
            Text(
                text = "Reminders for ${DateFormatHandler().getDayFromTimestamp(timestamp.time)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Reminder List or Empty State
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                val selectedFormattedDate =
                    selectedDate.value.format(DateTimeFormatter.ofPattern("EEE, dd MMM"))

                val filteredReminders = reminders.value.filter { reminder ->
                    DateFormatHandler().formatDate(reminder.time) == selectedFormattedDate && reminder.time >= System.currentTimeMillis()
                }

                if (filteredReminders.isEmpty()) {
                    // Empty State UI

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsOff,
                                contentDescription = "No Reminders",
                                tint = MaterialTheme.colorScheme.onTertiary.copy(0.7f),
                                modifier = Modifier.size(35.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No Reminders Found",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add a reminder to stay on top of your tasks.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                } else {
                    // Populate Reminders
                    items(filteredReminders, key = { it.id }) { reminder ->
                        ReminderDetailsCard(
                            reminder = reminder,
                            isReminderEnabled = true,
                            onSwitchToggle = {},
                            onClick = {
                                Log.d("Tag", "HomeScreen - ReminderValue --> " + reminder.id)
                                reminderId.value = reminder.id
                                showUpdateReminder.value = true
                                onNavigateToUpdate(reminder.id)
                            },
                            onDelete = { reminderViewModel.deleteReminder(reminder) }
                        )
                    }
                }
            }
        }

        // Bottom Sheet for Adding Reminders
        if (showBottomSheet.value) {
            AddReminder(
                mainViewModel = mainViewModel,
                onClickCancel = { showBottomSheet.value = false },
                onClickSave = {

                    if (mainViewModel.time.value >= System.currentTimeMillis()) {

                        showBottomSheet.value = false
                        var reminderTitle =
                            if (it) mainViewModel.aiReminderTitle.value else mainViewModel.reminderTittle.value
                        var reminderTimestamp = if (it) DateFormatHandler().extractTimestamp(
                            reminderTitle.toLowerCase(Locale.ROOT)
                        ) else DateFormatHandler().mergeDateAndTime(
                            mainViewModel.date.value,
                            mainViewModel.time.value
                        )

                        val reminder = Reminder(
                            tittle = reminderTitle,
                            description = mainViewModel.reminderDesc.value,
                            time = reminderTimestamp!!,
                            tagColor = mainViewModel.tagColor.value.value.toLong(),
                            repeatingOptions = mainViewModel.repeatingOptions.value
                        )
                        reminderViewModel.insertReminder(reminder)
                    } else {
                        Toast.makeText(
                            context,
                            "Reminder time cannot be in the past!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        // Update Reminder Screen
        if (showUpdateReminder.value) {
//                AddReminder(
//                    mainViewModel = mainViewModel,
//                    reminderViewModel = reminderViewModel,
//                    onClickCancel = { showUpdateReminder.value = false },
//                    onClickSave = {
//                        showUpdateReminder.value = false
//                        val reminderTimeStamp = DateFormatHandler().mergeDateAndTime(
//                            mainViewModel.date.value,
//                            mainViewModel.time.value
//                        )
//                        val reminder = Reminder(
//                            id = reminderId.value,
//                            tittle = mainViewModel.reminderTittle.value,
//                            description = mainViewModel.reminderDesc.value,
//                            time = reminderTimeStamp,
//                            tagColor = mainViewModel.tagColor.value.value.toLong(),
//                            repeatingOptions = mainViewModel.repeatingOptions.value
//                        )
//                        updateOrInsertReminder(
//                            reminderViewModel,
//                            mainViewModel,
//                            context,
//                            true,
//                            reminderId.value,
//                            reminder
//                        )
//                    },
//                    id = reminderId.value
//                )
        }
    }
}

fun updateOrInsertReminder(
    reminderViewModel: ReminderViewModel,
    mainViewModel: MainViewModel,
    context: Context,
    updateFlag: Boolean,
    reminderId: Long,
    reminder1: Reminder
) {
    if (!mainViewModel.reminderTittle.value.isEmpty()) {
        if (updateFlag) {
            Log.d("TAg", "Reminder If#" + reminder1.id)
            reminderViewModel.updateReminder(reminder1)
            Toast.makeText(context, "Reminder Updated", Toast.LENGTH_SHORT).show()
        } else {
            reminderViewModel.insertReminder(reminder1)
        }
    } else {
        Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
    }
}
