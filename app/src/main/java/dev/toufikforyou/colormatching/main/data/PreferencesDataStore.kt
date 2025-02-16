package dev.toufikforyou.colormatching.main.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
        private val HIGH_SCORES = stringPreferencesKey("high_scores")
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

    val highScores: Flow<List<HighScoreEntry>> = dataStore.data.map { preferences ->
        val scoresStr = preferences[HIGH_SCORES] ?: ""
        if (scoresStr.isEmpty()) return@map emptyList()
        scoresStr.split(",").map { HighScoreEntry.fromJson(it) }.sortedByDescending { it.score }
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

    suspend fun updateHighScore(newScore: HighScoreEntry) {
        dataStore.edit { preferences ->
            val currentScores =
                (preferences[HIGH_SCORES] ?: "").split(",").filter { it.isNotEmpty() }
                    .map { HighScoreEntry.fromJson(it) }.toMutableList()

            currentScores.add(newScore)
            currentScores.sortByDescending { it.score }

            // Keep only top 10 scores
            val topScores = currentScores.take(10)
            preferences[HIGH_SCORES] = topScores.joinToString(",") { it.toJson() }
        }
    }
}

data class HighScoreEntry(
    val score: Int, val level: Int, val difficulty: String, val date: String
) {
    fun toJson(): String {
        return "$score|$level|$difficulty|$date"
    }

    companion object {
        fun fromJson(json: String): HighScoreEntry {
            val parts = json.split("|")
            return HighScoreEntry(
                score = parts[0].toInt(),
                level = parts[1].toInt(),
                difficulty = parts[2],
                date = parts[3]
            )
        }
    }
} 