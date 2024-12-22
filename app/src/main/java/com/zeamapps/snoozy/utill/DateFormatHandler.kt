package com.zeamapps.snoozy.utill

import androidx.compose.ui.graphics.Color
import com.joestelmach.natty.Parser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateFormatHandler {
    fun getDayFromTimestamp(timestamp: Long): String {
        // Get the current date and tomorrow's date
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Calculate tomorrow's date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time

        // Formatter to normalize dates (ignoring time part)
        val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        // Format input timestamp to normalized date
        val inputDate = Date(timestamp)
        val formattedInputDate = formatter.format(inputDate)

        // Compare the input date with today and tomorrow
        return when (formattedInputDate) {
            formatter.format(today) -> "Today"
            formatter.format(tomorrow) -> "Tomorrow"
            else -> formatDate(timestamp)
        }
    }
    fun formatDate(timestampMillis: Long): String {
        val sdf =
            SimpleDateFormat("EEE, dd MMM", Locale.getDefault()) // "EEE" for day, "MMM" for month
        val date = Date(timestampMillis)
        return sdf.format(date)
    }

    fun mergeDateAndTime(dateTimestamp: Long, timeTimestamp: Long): Long {
        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = dateTimestamp
        }
        val timeCalendar = Calendar.getInstance().apply {
            timeInMillis = timeTimestamp
        }
        val mergedCalendar = Calendar.getInstance().apply {
            set(
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH),
                timeCalendar.get(Calendar.HOUR_OF_DAY),
                timeCalendar.get(Calendar.MINUTE),
                timeCalendar.get(Calendar.SECOND)
            )
        }
        return mergedCalendar.timeInMillis // Return the merged timestamp
    }
    fun convertColorToLong(color: Color) = color.value.toLong()

    fun extractTimestamp(inputText: String): Long? {
        val parser = Parser()
        val dates = parser.parse(inputText).firstOrNull()?.dates
        return if (!dates.isNullOrEmpty()) {
            val extractedDate = dates[0]
            extractedDate.time // Returns the timestamp as a Long in milliseconds
        } else {
            null
        }
    }

    fun formatTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp) // Ensure the timestamp is in milliseconds
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12-hour format with am/pm
        return formatter.format(date).lowercase(Locale.getDefault()) // Lowercase for 'am/pm'
    }
}