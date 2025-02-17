package dev.toufikforyou.colormatching.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dev.toufikforyou.colormatching.main.data.local.dao.HighScoreDao
import dev.toufikforyou.colormatching.main.data.local.entity.HighScore
import kotlinx.coroutines.flow.Flow

class HighScoresViewModel(
    private val highScoreDao: HighScoreDao
) : ViewModel() {
    val highScores: Flow<List<HighScore>> = highScoreDao.getAllHighScores()

    fun getHighScoresByDifficulty(difficulty: String): Flow<List<HighScore>> {
        return highScoreDao.getHighScoresByDifficulty(difficulty)
    }
} 