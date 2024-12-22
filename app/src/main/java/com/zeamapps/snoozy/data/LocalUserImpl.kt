package com.zeamapps.snoozy.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.zeamapps.snoozy.domain.LocalUserManager
import com.zeamapps.snoozy.utill.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalUserImpl(var context: Context) : LocalUserManager {
    override suspend fun saveAppEntry() {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[PreferenceKey.APP_ENTRY] = true
        }
    }
    override fun readAppEntry(): Flow<Boolean> {
        return context.userPreferencesDataStore.data.map { preferences ->
            preferences[PreferenceKey.APP_ENTRY] == true
        }
    }
}

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.USER_SETTINGS
)

object PreferenceKey {
    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
}