package com.zeamapps.snoozy.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReminderApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}