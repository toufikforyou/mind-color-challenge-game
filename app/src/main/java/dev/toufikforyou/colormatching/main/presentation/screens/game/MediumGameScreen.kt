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
fun MediumGameScreen(
    navController: NavController,
    soundManager: SoundManager,
    isSoundEnabled: Boolean,
    preferencesDataStore: PreferencesDataStore
) {
    // Calculate initial time limit based on level ranges for medium difficulty
    fun calculateTimeLimit(level: Int) = when {
        level < 10 -> 60  // Level 1-9: 60 seconds
        level < 20 -> 50  // Level 10-19: 50 seconds
        level < 30 -> 45  // Level 20-29: 45 seconds
        level < 40 -> 40  // Level 30-39: 40 seconds
        level < 50 -> 35  // Level 40-49: 35 seconds
        else -> 30        // Level 50+: 30 seconds
    }

    var gameState by remember {
        mutableStateOf(
            GameState(
                gridSize = 4, // 4x4 grid for medium difficulty
                timeLimit = calculateTimeLimit(1), isGameStarted = false
            )
        )
    }

    val scope = rememberCoroutineScope()
    var showGameOverDialog by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(gameState.timeLimit) }
    var showInitialColors by remember { mutableStateOf(false) }

    var mutableColorBoxes by remember {
        mutableStateOf(generateColorPairs(gameState.gridSize))
    }

    val selectedBoxes = remember { mutableStateListOf<Int>() }

    // Calculate total pairs for 4x4 grid
    val totalPairs = remember(gameState.gridSize) {
        (gameState.gridSize * gameState.gridSize) / 2  // 8 pairs for 4x4
    }

    // Bonus points for quick matches
    var lastMatchTime by remember { mutableLongStateOf(0L) }

    // Collect high scores
    val highScores by preferencesDataStore.highScores.collectAsState(initial = emptyList())

    // Handle box selection function with combo bonus
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

                        // Calculate bonus points based on quick matches
                        val bonusPoints = when {
                            timeDiff < 2000 -> 15 // Quick match bonus
                            timeDiff < 3000 -> 10 // Medium speed bonus
                            else -> 5            // Base points
                        }

                        lastMatchTime = currentTime
                        val newMatchedPairs = gameState.matchedPairs + 1
                        gameState = gameState.copy(
                            matchedPairs = newMatchedPairs, score = gameState.score + bonusPoints
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
                    }
                }
            }
        }
    }

    // Add these state variables near other state declarations
    var showExitDialog by remember { mutableStateOf(false) }
    var isTimerPaused by remember { mutableStateOf(false) }

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
            delay(5000) // 5 seconds for medium mode
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
                difficulty = "Medium",
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
            difficulty = "Medium",
            level = gameState.currentLevel,
            highScores = highScores,
            onTryAgain = {
                showGameOverDialog = false
                gameState = GameState(
                    gridSize = 4, timeLimit = calculateTimeLimit(1), isGameStarted = false
                )
                timeLeft = gameState.timeLimit
                mutableColorBoxes = generateColorPairs(gameState.gridSize)
                selectedBoxes.clear()
                showInitialColors = false
            },
            onBack = {
                navController.navigateUp()
            })
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            MaterialTheme.colorScheme.background
        ), topBar = {
        GameAppBar(title = "Medium Level ${gameState.currentLevel}") {
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
                AnimatedGameScore("Score", gameState.score.toString())
                AnimatedGameScore("Level", gameState.currentLevel.toString())
            }

            // Color grid with smaller padding for 4x4
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

    // Add GameExitDialog
    if (showExitDialog) {
        GameExitDialog(onDismiss = {
            showExitDialog = false
            isTimerPaused = false  // Resume from current timeLeft
        }, onConfirm = {
            if (isSoundEnabled) {
                soundManager.playButtonClick()
            }
            navController.navigateUp()
        })
    }
}