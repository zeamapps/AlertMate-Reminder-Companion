package com.zeamapps.snoozy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.data.repository.ReminderRepo
import com.zeamapps.snoozy.worker.ReminderWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(
    var reminderRepo: ReminderRepo,
    var workManagerProvider: ReminderWorkManager
) : ViewModel() {
    var _reminderList = MutableStateFlow<List<Reminder>>(emptyList())
    val reminderList: StateFlow<List<Reminder>> get() = _reminderList

    init {
        viewModelScope.launch {
            reminderRepo.getAllReminders().collect {
                _reminderList.value = it
            }
        }
    }

    fun deleteReminder(reminder: Reminder) = executeInViewModelScope {
        reminderRepo.deleteReminder(reminder)
        workManagerProvider.addOrUpdateReminder(reminder, true)
    }

    fun updateReminder(reminder: Reminder) {
        Log.d("Reminder", "REMINDERUPDATED   ${reminder.tittle}")
        executeInViewModelScope {
            Log.d("Reminder","@#@REminderUpd -> "+reminder.tittle)
            reminderRepo.updateReminder(reminder)

           // workManagerProvider.addOrUpdateReminder(reminder, false)
        }
    }

    fun insertReminder(reminder: Reminder) {
        Log.d("Reminder", "REMINDERINSERTED   ${reminder.id}")

        executeInViewModelScope {
            var reminderId = reminderRepo.insertReminder(reminder)
            reminder.id = reminderId
            Log.d("Reminder", "REMINDERINSERTEDNEW   ${reminder.id}")
            workManagerProvider.addOrUpdateReminder(reminder, false)
        }
    }


    fun getReminderById(id: Long): Flow<Reminder> = reminderRepo.getRemindersById(id)

    private fun executeInViewModelScope(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error in ViewModel action: ${e.localizedMessage}", e)
            }
        }
    }
}