package dev.toufikforyou.colormatching.main.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDataStore(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_SOUND_ENABLED = booleanPreferencesKey("is_sound_enabled")
        val USE_SYSTEM_THEME = booleanPreferencesKey("use_system_theme")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE] ?: true
    }

    val isSoundEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_SOUND_ENABLED] ?: true
    }

    val useSystemTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[USE_SYSTEM_THEME] ?: true
    }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateSoundEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_SOUND_ENABLED] = isEnabled
        }
    }

    suspend fun updateUseSystemTheme(useSystem: Boolean) {
        dataStore.edit { preferences ->
            preferences[USE_SYSTEM_THEME] = useSystem
        }
    }
}