package dev.toufikforyou.colormatching.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.data.local.dao.GameProgressDao
import dev.toufikforyou.colormatching.main.domain.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val preferencesDataStore: PreferencesDataStore,
    val gameProgressDao: GameProgressDao,
    private val initialGridSize: Int,
    private val difficulty: String
) : ViewModel() {
    private val _gameState = MutableStateFlow(
        GameState(
            gridSize = initialGridSize,
            timeLimit = calculateTimeLimit(1),
            isGameStarted = false
        )
    )
    val gameState: StateFlow<GameState> = _gameState

    fun updateGameState(update: (GameState) -> GameState) {
        _gameState.value = update(_gameState.value)
    }

    fun calculateTimeLimit(level: Int): Int {
        return when (difficulty) {
            "Easy" -> when {
                level < 10 -> 20  // Level 1-9: 30 seconds
                level < 20 -> 18  // Level 10-19: 25 seconds
                level < 30 -> 16  // Level 20-29: 20 seconds
                level < 40 -> 14  // Level 30-39: 18 seconds
                level < 50 -> 12  // Level 40-49: 15 seconds
                level < 60 -> 10  // Level 50-59: 12 seconds
                level < 70 -> 8   // Level 60-69: 10 seconds
                level < 80 -> 6   // Level 70-79: 8 seconds
                else -> 4         // Level 80+: 6 seconds
            }
            "Medium" -> when {
                level < 10 -> 60
                level < 20 -> 50
                level < 30 -> 45
                level < 40 -> 40
                level < 50 -> 35
                else -> 30
            }
            "Hard" -> when {
                level < 10 -> 180
                level < 20 -> 150
                level < 30 -> 120
                level < 40 -> 100
                level < 50 -> 80
                level < 60 -> 60
                else -> 50
            }
            else -> 30
        }
    }

    // Add game logic methods here
} 