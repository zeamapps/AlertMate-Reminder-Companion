package com.zeamapps.snoozy.worker

import android.app.Application
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.presentation.components.RepeatingOptions
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ReminderWorkManager @Inject constructor(private val application: Application) {
    fun addOrUpdateReminder(reminder: Reminder, shouldDelete: Boolean) {
        val workManager = WorkManager.getInstance(application)
        Log.d("Tag", "Inside addOrUpdateReminder function" + reminder.id)

        // Cancel any existing work associated with this reminder
        workManager.cancelAllWorkByTag(reminder.id.toString())
        if (!shouldDelete) {
            val currentTimeMillis = System.currentTimeMillis()
            val delayMillis = reminder.time - currentTimeMillis
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()

            if (reminder.time > currentTimeMillis) {
                Log.d("Tag", "Current time: $currentTimeMillis")
                Log.d("Tag", "Reminder time: ${reminder.time}")
                Log.d("Tag", "Delay: $delayMillis")

                if (delayMillis > 0) {
                    val reminderWorkRequest = createWorkRequest(reminder, delayMillis)

                    workManager.enqueueUniqueWork(
                        "Reminder_${reminder.id}",
                        ExistingWorkPolicy.REPLACE,
                        reminderWorkRequest
                    )

                    Log.d(
                        "ReminderApp",
                        "Reminder scheduled: ${reminder.id} with delay $delayMillis ms"
                    )
                } else {
                    Log.d(
                        "ReminderApp",
                        "Skipping past reminder: ${reminder.id}, scheduled time has already passed"
                    )
                }
            } else {
                Log.e("Reminder", "Reminder time is in the past!")
            }
        } else {
            Log.d("ReminderApp", "Reminder marked for deletion: ${reminder.id}")
            // Additional deletion logic can be added here if necessary
        }
    }

    private fun createWorkRequest(reminder: Reminder, delayMillis: Long): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "title" to reminder.tittle,
                    "description" to reminder.description,
                    "reminderId" to reminder.id,
                    "repeatingOption" to reminder.repeatingOptions.name
                )
            )
            .addTag(reminder.id.toString()) // Ensure tagging for future reference
            .build()
    }

    fun rescheduleReminder(reminderId: Int, repeatingOption: RepeatingOptions) {

        val workManager = WorkManager.getInstance(application)
        val currentTimeMillis = System.currentTimeMillis()
        val nextTriggerTime = calculateNextTriggerTime(currentTimeMillis, repeatingOption)

        Log.d("ReminderApp", "Rescheduling reminder: $reminderId to next time: $nextTriggerTime")

        val reminderWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(nextTriggerTime - currentTimeMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "reminderId" to reminderId,
                    "repeatingOption" to repeatingOption.name
                )
            )
            .addTag(reminderId.toString())
            .build()

        workManager.enqueueUniqueWork(
            "Reminder_$reminderId",
            ExistingWorkPolicy.REPLACE,
            reminderWorkRequest
        )
    }

    private fun calculateNextTriggerTime(
        currentTime: Long,
        repeatingOption: RepeatingOptions
    ): Long {
        val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
        when (repeatingOption) {
            RepeatingOptions.DAILY -> calendar.add(Calendar.DAY_OF_YEAR, 1)
            RepeatingOptions.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
            RepeatingOptions.MONTHLY -> calendar.add(Calendar.MONTH, 1)
            RepeatingOptions.DO_NOT_REPEAT -> return currentTime // No repeat
        }
        return calendar.timeInMillis
    }

    fun snoozeReminder(
        notificationId: Int,
        delayMinutes: Long,
        reminderTittle: String?,
        reminderdesc: String?,
        repeatingOption: Serializable?,
        reminderId: Int
    ) {
        WorkManager.getInstance(application).cancelAllWorkByTag(reminderId.toString())
        Log.d("Tag", "Inside snoozeReminder function delay $delayMinutes")
        var workData = workDataOf(
            "NOTIFICATION_ID" to notificationId,
            "reminderTittle" to reminderTittle,
            "reminderdesc" to reminderdesc,
            "repeatingOption" to repeatingOption,
            "reminderId" to reminderId
        )
        Log.d("Tag", "Reminder id $reminderId")

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MILLISECONDS)
            .setInputData(workData).addTag(reminderId.toString())
            .build()
        WorkManager.getInstance(application).enqueueUniqueWork(
            "Reminder_${reminderId}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
