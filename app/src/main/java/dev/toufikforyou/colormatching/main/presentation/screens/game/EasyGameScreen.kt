package dev.toufikforyou.colormatching.main.presentation.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.main.domain.model.GameState
import dev.toufikforyou.colormatching.main.presentation.components.AnimatedGameScore
import dev.toufikforyou.colormatching.main.presentation.components.ColorGrid
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground
import dev.toufikforyou.colormatching.main.presentation.components.GameOverDialog
import dev.toufikforyou.colormatching.main.presentation.components.GameTimeScore
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.main.utils.generateColorPairs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyGameScreen(
    navController: NavController, soundManager: SoundManager, isSoundEnabled: Boolean
) {
    // Calculate initial time limit based on level ranges
    fun calculateTimeLimit(level: Int) = when {
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

    var gameState by remember {
        mutableStateOf(
            GameState(
                gridSize = 3, timeLimit = calculateTimeLimit(1), isGameStarted = false
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

    // Calculate total pairs for current grid size
    val totalPairs = remember(gameState.gridSize) {
        (gameState.gridSize * gameState.gridSize) / 2
    }

    // Handle box selection function
    val handleBoxSelection = { index: Int ->
        if (!mutableColorBoxes[index].isMatched && !selectedBoxes.contains(index) && selectedBoxes.size < 2) {
            // Update the selected state of the clicked box
            mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                if (i == index) {
                    box.copy(isSelected = true)
                } else {
                    box
                }
            }

            selectedBoxes.add(index)

            if (selectedBoxes.size == 2) {
                val firstIndex = selectedBoxes[0]
                val secondIndex = selectedBoxes[1]

                if (mutableColorBoxes[firstIndex].color == mutableColorBoxes[secondIndex].color) {
                    // Match found
                    scope.launch {
                        delay(300) // Short delay before showing match
                        if (isSoundEnabled) soundManager.playMatchFound()
                        mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                            if (i == firstIndex || i == secondIndex) {
                                box.copy(isMatched = true, isSelected = false)
                            } else {
                                box
                            }
                        }

                        val newMatchedPairs = gameState.matchedPairs + 1
                        gameState = gameState.copy(
                            matchedPairs = newMatchedPairs, score = gameState.score + 10
                        )

                        // Clear selected boxes after match
                        selectedBoxes.clear()

                        // Level up only if ALL pairs are matched
                        if (newMatchedPairs == totalPairs) {
                            if (isSoundEnabled) soundManager.playLevelComplete()
                            val nextLevel = gameState.currentLevel + 1
                            // Prepare next level
                            gameState = gameState.copy(
                                currentLevel = nextLevel,
                                timeLimit = calculateTimeLimit(nextLevel),
                                matchedPairs = 0,
                                isGameStarted = false
                            )

                            // Reset game state for new level
                            timeLeft = gameState.timeLimit
                            mutableColorBoxes = generateColorPairs(gameState.gridSize)
                            showInitialColors = true
                        }
                    }
                } else {
                    // No match - hide cards after delay
                    scope.launch {
                        delay(500)
                        mutableColorBoxes = mutableColorBoxes.mapIndexed { i, box ->
                            if (i == firstIndex || i == secondIndex) {
                                box.copy(isSelected = false)
                            } else {
                                box
                            }
                        }
                        selectedBoxes.clear()
                    }
                }
            }
        }
    }

    // Game timer
    LaunchedEffect(gameState.isGameStarted, gameState.currentLevel) {
        if (gameState.isGameStarted) {
            timeLeft = gameState.timeLimit
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
            delay(3000) // 3 seconds for easy mode
            showInitialColors = false
            gameState = gameState.copy(isGameStarted = true)
        }
    }

    if (showGameOverDialog) {
        GameOverDialog(score = gameState.score,
            matchedPairs = gameState.matchedPairs,
            totalPairs = totalPairs,
            onTryAgain = {
                showGameOverDialog = false
                gameState = GameState(
                    gridSize = 3, timeLimit = calculateTimeLimit(1), isGameStarted = false
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
        .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = {
                Text(
                    "Easy Level ${gameState.currentLevel}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
            )
        }) { paddingValues ->
        GameBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
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

            // Color grid
            ColorGrid(gridSize = gameState.gridSize,
                colorBoxes = mutableColorBoxes,
                showInitialColors = showInitialColors,
                onBoxClick = { index ->
                    if (!showInitialColors && gameState.isGameStarted && !mutableColorBoxes[index].isMatched) {
                        handleBoxSelection(index)
                    }
                })

            if (!gameState.isGameStarted && !showGameOverDialog && !showInitialColors) {
                Button(
                    onClick = { showInitialColors = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Start Game",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}