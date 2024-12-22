package com.zeamapps.snoozy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zeamapps.snoozy.data.models.Reminder

@Database(entities = [Reminder::class], version = 3, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase(){
abstract fun reminderDao(): ReminderDao
}