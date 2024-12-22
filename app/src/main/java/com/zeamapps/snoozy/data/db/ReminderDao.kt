package com.zeamapps.snoozy.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zeamapps.snoozy.data.models.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertReminder(reminder: Reminder): Long
    @Delete
    abstract suspend fun deleteReminder(reminder: Reminder)
    @Update
    abstract suspend fun updateReminder(reminder: Reminder)
    @Query("select * from `reminders-table`")
    abstract  fun getAllReminders(): Flow<List<Reminder>>
    @Query("select * from `reminders-table` where id=:id")
    abstract fun getReminderById(id: Long): Flow<Reminder>
}