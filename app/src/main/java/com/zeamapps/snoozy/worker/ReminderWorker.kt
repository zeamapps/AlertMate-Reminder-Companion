package com.zeamapps.snoozy.worker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker

import androidx.work.WorkerParameters
import com.zeamapps.snoozy.notification.NotificationHelper.showNotification
import com.zeamapps.snoozy.presentation.components.RepeatingOptions


class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {

        val title = inputData.getString("title")
        val description = inputData.getString("description")
        var reminderId = inputData.getLong("reminderId", 0L)
        Log.d("Tag", "Inside ReminderWorker $title")

        Log.d("reminderid", "ReminderId -->$reminderId")
        val repeatingOptionName =
            inputData.getString("repeatingOption") ?: RepeatingOptions.DO_NOT_REPEAT.name
        var repeatingOptions = RepeatingOptions.valueOf(repeatingOptionName)
        Log.d("ReminderWorker", "Triggering notification for: $title")
        if (title != null && description != null) {
            showNotification(
                applicationContext,
                title,
                description,
              1, repeatingOptions
            )

            if (repeatingOptions.name != RepeatingOptions.DO_NOT_REPEAT.name) {
                Log.d("Tag", "Inside Repeating -> ${repeatingOptions.name}")
                var reminderWorker =
                    ReminderWorkManager(application = applicationContext as Application)
                reminderWorker.rescheduleReminder(reminderId!!.toInt(), repeatingOptions)
            }

            return Result.success()
        }
        return Result.retry()
    }
}


