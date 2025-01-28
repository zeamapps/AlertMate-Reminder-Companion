package com.zeamapps.snoozy.presentation


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.presentation.home.ReminderDetailsCard

import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
import com.zeamapps.snoozy.utill.DateFormatHandler
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(reminderViewModel: ReminderViewModel) {
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val selectedDate = remember { mutableStateOf(getCurrentDate()) }
    val reminders by reminderViewModel.reminderList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Month Navigation
        MonthSelector(currentMonth) { newMonth -> currentMonth.value = newMonth }

        Spacer(modifier = Modifier.height(12.dp))

        // Calendar View
        CalendarView(currentMonth.value, selectedDate, reminders)

        Spacer(modifier = Modifier.height(16.dp))

        // Show reminders for selected date
        RemindersList(selectedDate.value.toString(), reminders)
    }
}

@Composable
fun MonthSelector(currentMonth: MutableState<YearMonth>, onMonthChange: (YearMonth) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.value.minusMonths(1)) }) {
            Text(text = "<", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "${currentMonth.value.month.name} ${currentMonth.value.year}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        IconButton(onClick = { onMonthChange(currentMonth.value.plusMonths(1)) }) {
            Text(text = ">", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CalendarView(
    currentMonth: YearMonth,
    selectedDate: MutableState<String>,
    reminders: List<Reminder>
) {
    val daysInMonth = remember { generateDatesFromMonth(currentMonth) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(daysInMonth) { date ->
            val hasReminder = reminders.any { it.dateFormatted == date }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        if (selectedDate.value == date) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { selectedDate.value = date },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date.split("-").last(),
                        fontWeight = FontWeight.Bold,
                        color = if (selectedDate.value == date) Color.White else MaterialTheme.colorScheme.onBackground
                    )
                    if (hasReminder) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemindersList(selectedDate: String, reminders: List<Reminder>) {
    val filteredReminders = reminders.filter { it.dateFormatted == selectedDate }

    if (filteredReminders.isEmpty()) {
        Text(
            text = "No reminders for this date",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    } else {
        Column {
            filteredReminders.forEach { reminder ->
                ReminderDetailsCard(reminder, isReminderEnabled = true, onClick = {}, onDelete = {})
            }
        }
    }
}

// Helper function to generate days in the selected month
fun generateDatesFromMonth(month: YearMonth): List<String> {
    return (1..month.lengthOfMonth()).map { day ->
        String.format("%04d-%02d-%02d", month.year, month.monthValue, day)
    }
}

// Extension for formatting reminder date
val Reminder.dateFormatted: String
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(time))

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
}

@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarWithReminders(
    month: YearMonth,
    reminders: List<Reminder>,
    onDateSelected: (LocalDate) -> Unit = {}
) {
    // State to track the selected date
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Calendar Grid
        CalendarGridView(
            month = month,
            reminders = reminders,
            onDateSelected = { date ->
                selectedDate.value = date
                onDateSelected(date)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display reminders for the selected date
        selectedDate.value?.let { date ->
            val filteredReminders = reminders.filter { reminder ->
                val reminderDate = LocalDate.ofInstant(
                    Instant.ofEpochMilli(reminder.time),
                    ZoneId.systemDefault()
                )
                reminderDate == date
            }

            if (filteredReminders.isNotEmpty()) {
                Text(
                    text = "Reminders for ${date.dayOfMonth} ${date.month.name.lowercase().capitalize()} ${date.year}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    items(filteredReminders) { reminder ->
                        ReminderDetailsCard(reminder,true,{},{}) { }
                    }
                }
            } else {
                Text(
                    text = "No reminders for this date.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        } ?: Text(
            text = "Select a date to view reminders.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}


@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarGridView(
    month: YearMonth,
    reminders: List<Reminder>,
    onDateSelected: (LocalDate) -> Unit
) {
    // Generate all dates for the given month
    val daysInMonth = remember { generateDatesForMonth(month) }

    LazyVerticalGrid(
        GridCells.Fixed(7), // 7 columns for a week
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(daysInMonth) { date ->
            if (date != null) {
                // Check if the current date has reminders
                val hasReminders = reminders.any { reminder ->
                    val reminderDate = LocalDate.ofInstant(
                        Instant.ofEpochMilli(reminder.time),
                        ZoneId.systemDefault()
                    )
                    reminderDate == date
                }

                CalendarDay(
                    date = date,
                    isHighlighted = hasReminders,
                    onClick = { onDateSelected(date) }
                )
            } else {
                // Render an empty placeholder for null dates
                Spacer(
                    modifier = Modifier
                        .size(48.dp) // Match the size of CalendarDay for consistency
                )
            }
        }
    }
}
@Composable
fun CalendarDay(
    date: LocalDate,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(48.dp) // Define cell size
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the date
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        // Display a dot if reminders exist for this date
        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(top = 4.dp) // Adjust the position of the dot
            )
        }
    }
}

fun generateDatesForMonth(month: YearMonth): List<LocalDate?> {
    val dates = mutableListOf<LocalDate?>()
    val firstDayOfMonth = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()

    // Determine the starting day of the week for the first day of the month
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0, Saturday = 6

    // Add placeholders for days before the first day of the current month
    repeat(startDayOfWeek) {
        dates.add(null)
    }

    // Add all days of the current month
    for (day in 1..daysInMonth) {
        dates.add(month.atDay(day))
    }

    // Fill the remaining cells with placeholders to complete the last row
    while (dates.size % 7 != 0) {
        dates.add(null)
    }

    return dates
}