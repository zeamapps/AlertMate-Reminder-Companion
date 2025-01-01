package com.zeamapps.snoozy.notification

import android.Manifest
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.zeamapps.snoozy.worker.ReminderWorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_MARK_AS_DONE" -> {
                val notificationId = intent.getIntExtra("NOTIFICATION_ID", -1)
                // Validate the "Mark as Done" action
                Log.d("NotificationActionReceiver", "Mark as Done action received")
                Toast.makeText(context, "Marked as Done!", Toast.LENGTH_SHORT).show()
                // Perform your logic, e.g., mark a task as completed
                NotificationManagerCompat.from(context).cancel(notificationId)
            }

            "ACTION_SNOOZE" -> {
                val notificationId = intent.getIntExtra("NOTIFICATION_ID", -1)
                val reminderTittle = intent.getStringExtra("reminderTittle")
                val reminderdesc = intent.getStringExtra("reminderdesc")
                val reminderId = intent.getIntExtra("reminderId", -1)
                val repeatingOption = intent.getStringExtra("repeatingAction")

                Log.d("Tag", "Notification_Id -> " + notificationId)
                Log.d("Tag", "RepeatingOption -> " + repeatingOption)

                NotificationManagerCompat.from(context).cancel(notificationId)
                Toast.makeText(context, "Snoozed for 10 minutes!", Toast.LENGTH_SHORT).show()
                ReminderWorkManager(context.applicationContext as Application).snoozeReminder(
                    notificationId,
                    System.currentTimeMillis() + 5 * 60 * 1000,
                    reminderTittle,
                    reminderdesc,
                    repeatingOption,
                    reminderId
                )
                Log.d(
                    "NotificationActionReceiver",
                    "Snoozing for 5 minutes..." + System.currentTimeMillis() + 5 * 60 * 1000
                )

            }
        }
    }
}

