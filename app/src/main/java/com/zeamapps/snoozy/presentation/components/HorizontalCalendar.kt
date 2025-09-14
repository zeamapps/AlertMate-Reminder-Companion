package com.zeamapps.snoozy.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HorizontalCalendar() {

}

@RequiresApi(Build.VERSION_CODES.O)
fun generateDatesFromCurrentDate(): List<LocalDate> {
    val today = LocalDate.now()
    val currentYearMonth = YearMonth.from(today)

    val result = mutableListOf<LocalDate>()
    var yearMonth = currentYearMonth

    // Loop through months until December 2050
    while (yearMonth.year <= 2050) {
        val startDay = if (yearMonth == currentYearMonth) {
            today.dayOfMonth // Start from today's date if it matches the current month and year
        } else {
            1 // Otherwise, start from the first day of the month
        }

        val daysInMonth = yearMonth.lengthOfMonth()
        for (day in startDay..daysInMonth) {
            result.add(yearMonth.atDay(day))
        }

        // Move to the next month
        yearMonth = yearMonth.plusMonths(1)
    }
    return result
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateCard(
    date: LocalDate,
    isSelected: Boolean,
    hasReminder: Boolean, // Flag to indicate reminders
    onClick: () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.7f
        )

    var higlightedColor = if(isSelected)textColor else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .size(60.dp)
            ,
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp).clickable {
                    onClick()
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Day of the Week (e.g., "Mon")
            Text(
                text = date.dayOfWeek.name.take(3),
                color = textColor.copy(0.6f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            // Day of the Month
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            // Reminder Dot (conditionally displayed)
            if (hasReminder) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(higlightedColor,CircleShape)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlySelectorExample() {
    var selectedMonth = remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        MonthlyViewSelector(
            currentMonth = selectedMonth.value,
            onMonthChange = { newMonth ->
                selectedMonth.value = newMonth
                println("Selected Month: $newMonth")
            }
        )
    }
}

@Composable
fun MonthlyViewSelector(
    currentMonth: YearMonth = YearMonth.now(),
    onMonthChange: (YearMonth) -> Unit
) {
    var expanded = remember { mutableStateOf(false) }
    val months = Month.values()
    val yearRange = (currentMonth.year - 5)..(currentMonth.year + 5) // Customizable range
    var selectedMonth = remember { mutableStateOf(currentMonth) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = { expanded.value = true }) {
            Text(
                text = "${
                    selectedMonth.value.month.name.lowercase().replaceFirstChar { it.uppercase() }
                } ${selectedMonth.value.year}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            for (year in yearRange) {
                months.forEach { month ->
                    val monthYear = YearMonth.of(year, month)
                    DropdownMenuItem(text = {
                        Text(
                            text = "${
                                month.name.lowercase().replaceFirstChar { it.uppercase() }
                            } $year",
                            color = Color.Black
                        )
                    }, onClick = {
                        selectedMonth.value = monthYear
                        expanded.value = false
                        onMonthChange(monthYear)
                    })

                }
            }
        }
    }
}
