package com.zeamapps.snoozy.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.sql.Timestamp
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    reminderViewModel: ReminderViewModel,
    setingsBtnClicked: () -> Unit
) {
    val context = LocalContext.current
    var showBottomSheet = remember { mutableStateOf(false) }
    var showUpdateReminder = remember { mutableStateOf(false) }
    var currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = remember(currentMonth) { generateDatesFromCurrentDate() }
    var selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val localDateTime = selectedDate.value.atStartOfDay()
    val timestamp = Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    val reminderId = remember { mutableStateOf(0L) }
    mainViewModel.date.value = timestamp.time
    val reminders = reminderViewModel.reminderList.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val hasPermission = getNotificationPermissionState(context).collectAsState()

    Scaffold(
        floatingActionButton = {
            PulsedFloatingActionBtn {
                showBottomSheet.value = true
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppBar(Constants.APP_NAME) {
                setingsBtnClicked()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Horizontal Date Selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                style = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp),
                modifier = Modifier.padding(16.dp)
            )

            // Formatted Date
            val selectedFormattedDate =
                selectedDate.value.format(DateTimeFormatter.ofPattern("EEE, dd MMM"))

            // Reminder List or Empty State
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val filteredReminders = reminders.value.filter { reminder ->
                    DateFormatHandler().formatDate(reminder.time) == selectedFormattedDate
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
                        Log.d("Reminder","REMINDERID@### "+reminder.id)
                        ReminderDetailsCard(
                            reminder = reminder,
                            isReminderEnabled = true,
                            onSwitchToggle = {},
                            onClick = {
                                showUpdateReminder.value = true
                                reminderId.value = reminder.id
                            },
                            onDelete = {
                                reminderViewModel.deleteReminder(reminder)
                            }
                        )
                    }
                }
            }
        }

        // Bottom Sheet for Adding Reminders
        if (showBottomSheet.value) {
            AddReminder(mainViewModel, reminderViewModel, {
                showBottomSheet.value = false
            }, {

                if(!mainViewModel.reminderTittle.value.isEmpty()) {
                    val reminderTimeStamp = DateFormatHandler().mergeDateAndTime(
                        mainViewModel.date.value,
                        mainViewModel.time.value
                    )
                    val reminder = Reminder(
                        tittle = mainViewModel.reminderTittle.value,
                        description = mainViewModel.reminderDesc.value,
                        time = reminderTimeStamp,
                        tagColor = mainViewModel.tagColor.value.value.toLong(),
                        repeatingOptions = mainViewModel.repeatingOptions.value
                    )
                    Log.d(
                        "Reminder",
                        "ReminderCurrentTime : " + reminderTimeStamp + " : " + mainViewModel.time.value
                    )
                    reminderViewModel.insertReminder(reminder)
                    //   Toast.makeText(localContext, "Reminder Added.", Toast.LENGTH_SHORT).show()
                    showBottomSheet.value = false
                }else{
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }, 0L)
        }
        // Update Reminder Screen
        if (showUpdateReminder.value) {
            AddReminder(
                mainViewModel = mainViewModel,
                reminderViewModel = reminderViewModel,
                onClickCancel = { showUpdateReminder.value = false },
                onClickSave = { showUpdateReminder.value = false },
                id = reminderId.value
            )
        }
    }
}


//@Composable
//fun SettingsScreen(
//    onNavigateBack: () -> Unit,
//    onNotificationSettingsClick: () -> Unit,
//    onThemeToggle: (Boolean) -> Unit
//) {
//    Scaffold(
//        topBar = {
//            AppBar(
//                title = "Settings",
//                onBackBtnClicked = onNavigateBack
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            // General Settings
//            SettingsCategory("General")
//            SettingItem(
//                title = "Notifications",
//                subtitle = "Manage notification preferences",
//                onClick = onNotificationSettingsClick
//            )
//            SettingItem(
//                title = "Theme",
//                subtitle = "Switch between Light and Dark mode",
//                onClick = { onThemeToggle(/* isDarkMode */ true) }
//            )
//
//            // Reminder Preferences
//            SettingsCategory("Reminder Preferences")
//            SettingItem(
//                title = "Default Reminder Time",
//                subtitle = "Set default time for new reminders",
//                onClick = { /* Open time picker */ }
//            )
//
//            // Add other sections...
//        }
//    }
//}
//
//@Composable
//fun SettingsCategory(title: String) {
//    Text(
//        text = title,
//        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
//        modifier = Modifier.padding(16.dp)
//    )
//}
//
//@Composable
//fun SettingItem(
//    title: String,
//    subtitle: String,
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = title, style = MaterialTheme.typography.bodyLarge)
//            Text(
//                text = subtitle,
//                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
//            )
//        }
//        Icon(
//            imageVector = Icons.Default.ChevronRight,
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onBackground
//        )
//    }
//}
