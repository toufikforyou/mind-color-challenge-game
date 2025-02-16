package dev.toufikforyou.colormatching.main.presentation.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.main.data.HighScoreEntry
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.domain.model.GameState
import dev.toufikforyou.colormatching.main.presentation.components.AnimatedGameScore
import dev.toufikforyou.colormatching.main.presentation.components.ColorGrid
import dev.toufikforyou.colormatching.main.presentation.components.GameAppBar
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground
import dev.toufikforyou.colormatching.main.presentation.components.GameExitDialog
import dev.toufikforyou.colormatching.main.presentation.components.GameOverDialog
import dev.toufikforyou.colormatching.main.presentation.components.GameStartButton
import dev.toufikforyou.colormatching.main.presentation.components.GameTimeScore
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.main.utils.generateColorPairs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HardGameScreen(
    navController: NavController,
    soundManager: SoundManager,
    isSoundEnabled: Boolean,
    preferencesDataStore: PreferencesDataStore
) {
    // Calculate initial time limit based on level ranges for hard difficulty
    fun calculateTimeLimit(level: Int) = when {
        level < 10 -> 180 // Level 1-9: 180 seconds
        level < 20 -> 150 // Level 10-19: 150 seconds
        level < 30 -> 120 // Level 20-29: 120 seconds
        level < 40 -> 100 // Level 30-39: 100 seconds
        level < 50 -> 80  // Level 40-49: 80 seconds
        level < 60 -> 60  // Level 50-59: 60 seconds
        else -> 50         // Level 80+: 20 seconds
    }

    var gameState by remember {
        mutableStateOf(
            GameState(
                gridSize = 5, // 5x5 grid for hard difficulty
                timeLimit = calculateTimeLimit(1), isGameStarted = false
            )
        )
    }

    val scope = rememberCoroutineScope()
    var showGameOverDialog by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(gameState.timeLimit) }
    var showInitialColors by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var isTimerPaused by remember { mutableStateOf(false) }

    var mutableColorBoxes by remember {
        mutableStateOf(generateColorPairs(gameState.gridSize))
    }

    val selectedBoxes = remember { mutableStateListOf<Int>() }

    // Calculate total pairs for 5x5 grid
    val totalPairs = remember(gameState.gridSize) {
        (gameState.gridSize * gameState.gridSize) / 2  // 12 pairs + 1 unpaired for 5x5
    }

    // Streak and combo system
    var currentStreak by remember { mutableIntStateOf(0) }
    var maxStreak by remember { mutableIntStateOf(0) }
    var lastMatchTime by remember { mutableLongStateOf(0L) }

    // Collect high scores
    val highScores by preferencesDataStore.highScores.collectAsState(initial = emptyList())

    // Handle box selection function with enhanced scoring
    val handleBoxSelection = { index: Int ->
        if (!mutableColorBoxes[index].isMatched && !selectedBoxes.contains(index) && selectedBoxes.size < 2) {
            mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                if (i == index) box.copy(isSelected = true) else box
            }

            selectedBoxes.add(index)

            if (selectedBoxes.size == 2) {
                val firstIndex = selectedBoxes[0]
                val secondIndex = selectedBoxes[1]

                if (mutableColorBoxes[firstIndex].color == mutableColorBoxes[secondIndex].color) {
                    scope.launch {
                        delay(300)
                        if (isSoundEnabled) soundManager.playMatchFound() // Play match sound
                        mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                            if (i == firstIndex || i == secondIndex) {
                                box.copy(isMatched = true, isSelected = false)
                            } else box
                        }

                        val currentTime = System.currentTimeMillis()
                        val timeDiff = currentTime - lastMatchTime

                        // Update streak
                        currentStreak++
                        maxStreak = maxOf(maxStreak, currentStreak)

                        // Calculate bonus points based on streak and time
                        val streakBonus = currentStreak * 5
                        val timeBonus = when {
                            timeDiff < 1500 -> 20 // Super quick match
                            timeDiff < 2500 -> 15 // Quick match
                            timeDiff < 3500 -> 10 // Medium speed
                            else -> 5             // Base points
                        }

                        lastMatchTime = currentTime
                        val newMatchedPairs = gameState.matchedPairs + 1
                        gameState = gameState.copy(
                            matchedPairs = newMatchedPairs,
                            score = gameState.score + timeBonus + streakBonus
                        )

                        selectedBoxes.clear()

                        if (newMatchedPairs == totalPairs) {
                            val nextLevel = gameState.currentLevel + 1
                            gameState = gameState.copy(
                                currentLevel = nextLevel,
                                timeLimit = calculateTimeLimit(nextLevel),
                                matchedPairs = 0,
                                isGameStarted = false
                            )

                            timeLeft = gameState.timeLimit
                            mutableColorBoxes = generateColorPairs(gameState.gridSize)
                            currentStreak = 0 // Reset streak for new level
                            showInitialColors = true
                            if (isSoundEnabled) soundManager.playLevelComplete() // Play level complete sound
                        }
                    }
                } else {
                    scope.launch {
                        delay(500)
                        mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                            if (i == firstIndex || i == secondIndex) {
                                box.copy(isSelected = false)
                            } else box
                        }
                        selectedBoxes.clear()
                        currentStreak = 0 // Reset streak on mismatch
                    }
                }
            }
        }
    }

    // Game timer
    LaunchedEffect(gameState.isGameStarted, gameState.currentLevel, isTimerPaused) {
        if (gameState.isGameStarted && !isTimerPaused) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
                if (timeLeft == 0) {
                    gameState = gameState.copy(isGameStarted = false)
                    showGameOverDialog = true
                }
            }
        }
    }

    // Initial color display timer
    LaunchedEffect(showInitialColors) {
        if (showInitialColors) {
            delay(7000) // 7 seconds for hard mode
            showInitialColors = false
            gameState = gameState.copy(isGameStarted = true)
        }
    }

    // Add BackHandler
    BackHandler {
        if (gameState.isGameStarted) {
            showExitDialog = true
            isTimerPaused = true
        } else {
            navController.navigateUp()
        }
    }

    if (showGameOverDialog) {
        // Create new high score entry when game is over
        LaunchedEffect(Unit) {
            val newScore = HighScoreEntry(
                score = gameState.score,
                level = gameState.currentLevel,
                difficulty = "Hard",
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            // Only update if it's a high score
            if (highScores.size < 10 || highScores.any { it.score <= newScore.score }) {
                preferencesDataStore.updateHighScore(newScore)
            }
        }

        GameOverDialog(score = gameState.score,
            matchedPairs = gameState.matchedPairs,
            totalPairs = totalPairs,
            difficulty = "Hard",
            level = gameState.currentLevel,
            highScores = highScores,
            onTryAgain = {
                showGameOverDialog = false
                gameState = GameState(
                    gridSize = 5, timeLimit = calculateTimeLimit(1), isGameStarted = false
                )
                timeLeft = gameState.timeLimit
                mutableColorBoxes = generateColorPairs(gameState.gridSize)
                selectedBoxes.clear()
                currentStreak = 0
                maxStreak = 0
                showInitialColors = false
            },
            onBack = {
                navController.navigateUp()
            })
    }

    if (showExitDialog) {
        GameExitDialog(onDismiss = {
            showExitDialog = false
            isTimerPaused = false
        }, onConfirm = {
            if (isSoundEnabled) {
                soundManager.playButtonClick()
            }
            navController.navigateUp()
        })
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        topBar = {
            GameAppBar(title = "Hard Level ${gameState.currentLevel}") {
                if (gameState.isGameStarted) {
                    showExitDialog = true
                    isTimerPaused = true
                } else {
                    navController.navigateUp()
                }
            }
        }) { padding ->
        GameBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GameTimeScore(title = "Time", timeLeft = timeLeft)
                AnimatedGameScore(title = "Score", value = gameState.score.toString())
                AnimatedGameScore("Streak", "$currentStreak/$maxStreak")
            }

            // Color grid with smaller padding for 5x5
            ColorGrid(gridSize = gameState.gridSize,
                colorBoxes = mutableColorBoxes,
                showInitialColors = showInitialColors,
                onBoxClick = { index ->
                    if (!showInitialColors && gameState.isGameStarted && !mutableColorBoxes[index].isMatched) {
                        handleBoxSelection(index)
                    }
                })

            if (!gameState.isGameStarted && !showGameOverDialog && !showInitialColors) {
                GameStartButton {
                    showInitialColors = true
                }
            }
        }
    }
}