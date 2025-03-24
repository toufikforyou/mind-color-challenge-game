package dev.toufikforyou.colormatching.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.toufikforyou.colormatching.main.data.local.dao.GameProgressDao
import dev.toufikforyou.colormatching.main.data.local.dao.HighScoreDao
import dev.toufikforyou.colormatching.main.data.local.entity.HighScore
import dev.toufikforyou.colormatching.main.domain.model.GameState
import dev.toufikforyou.colormatching.main.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameViewModel(
    val gameProgressDao: GameProgressDao,
    val highScoreDao: HighScoreDao,
    private val notificationHelper: NotificationHelper,
    initialGridSize: Int,
    private val difficulty: String
) : ViewModel() {
    private val _gameState = MutableStateFlow(
        GameState(
            gridSize = initialGridSize, timeLimit = calculateTimeLimit(1), isGameStarted = false
        )
    )
    val gameState: StateFlow<GameState> = _gameState

    fun updateGameState(update: (GameState) -> GameState) {
        _gameState.value = update(_gameState.value)
        if (!_gameState.value.isGameStarted) {  // Only save high score when game is over
            viewModelScope.launch {
                saveHighScore(
                    score = _gameState.value.score,
                    level = _gameState.value.currentLevel,
                    difficulty = difficulty
                )
            }
        }
    }

    fun calculateTimeLimit(level: Int): Int {
        return when (difficulty) {
            "Easy" -> when {
                level < 10 -> 20
                level < 20 -> 18
                level < 30 -> 16
                level < 40 -> 14
                level < 50 -> 12
                level < 60 -> 10
                level < 70 -> 8
                level < 80 -> 6
                else -> 5
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

    suspend fun saveHighScore(score: Int, level: Int, difficulty: String) {
        val currentHighScore = highScoreDao.getHighScoresByDifficulty(difficulty).firstOrNull()
            ?.maxByOrNull { it.score }?.score ?: 0

        if (score > currentHighScore) {
            val newScore = HighScore(
                score = score,
                level = level,
                difficulty = difficulty,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )
            highScoreDao.insertHighScore(newScore)
            highScoreDao.deleteOldScores(difficulty, newScore.score)
        }
    }
}