package com.zeamapps.snoozy.data.repository

import com.zeamapps.snoozy.data.db.ReminderDao
import com.zeamapps.snoozy.data.models.Reminder
import javax.inject.Inject

class ReminderRepo @Inject constructor(private var reminderDao: ReminderDao) {
     fun getAllReminders() = reminderDao.getAllReminders()
    fun getRemindersById(id: Long) = reminderDao.getReminderById(id)
    suspend fun insertReminder(reminder: Reminder) : Long{
        return reminderDao.insertReminder(reminder)
    }
    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }
    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }
}