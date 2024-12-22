package com.zeamapps.snoozy.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeamapps.snoozy.utill.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(var application: Application) : ViewModel() {
    object PreferenceKey {
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    fun readTheme(): Flow<ThemeMode> {
        return application.themeStore.data.map { preferences ->
           ThemeMode.valueOf(preferences[PreferenceKey.THEME_MODE] ?: ThemeMode.SYSTEM_DEFAULT.name)
        }
    }


    fun saveTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            application.themeStore.edit { preferences ->
                preferences[PreferenceKey.THEME_MODE] = themeMode.name
            }
        }
    }
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM_DEFAULT
}

val Context.themeStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.THEME_STORE
)
