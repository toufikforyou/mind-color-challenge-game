package dev.toufikforyou.colormatching.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dev.toufikforyou.colormatching.main.data.HighScoreEntry
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import kotlinx.coroutines.flow.Flow

class HighScoresViewModel(
    preferencesDataStore: PreferencesDataStore
) : ViewModel() {
    val highScores: Flow<List<HighScoreEntry>> = preferencesDataStore.highScores
} 