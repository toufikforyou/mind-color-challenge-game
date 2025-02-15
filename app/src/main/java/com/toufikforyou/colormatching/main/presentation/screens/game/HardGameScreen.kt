package com.toufikforyou.colormatching.main.presentation.screens.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.toufikforyou.colormatching.main.domain.model.GameState
import com.toufikforyou.colormatching.main.presentation.animation.rememberFloatingParticle
import com.toufikforyou.colormatching.main.presentation.components.ColorGrid
import com.toufikforyou.colormatching.main.presentation.components.GameOverDialog
import com.toufikforyou.colormatching.main.utils.generateColorPairs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardGameScreen(navController: NavController) {
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
            delay(7000) // 7 seconds for hard mode
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        // Background particles effect
        GameBackground()

        Scaffold(containerColor = Color.Transparent, topBar = {
            TopAppBar(title = {
                Text(
                    "Hard Level ${gameState.currentLevel}",
                    style = MaterialTheme.typography.headlineMedium.copy(
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
        }) { padding ->
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
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Time", style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            )
                            TimerDisplay(timeLeft)
                        }
                    }
                    AnimatedGameStatCard("Score", gameState.score.toString())
                    AnimatedGameStatCard("Streak", "$currentStreak\n(Max: $maxStreak)")
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
                    Button(
                        onClick = { showInitialColors = true },
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Start Game", style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GameBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(20.dp)
    ) {
        repeat(20) { _ /* index */ ->
            val offset = rememberFloatingParticle()
            val size = remember { Random.nextInt(8, 17).dp }
            val alpha = remember { Random.nextFloat() * (0.15f - 0.05f) + 0.05f }

            Box(
                modifier = Modifier
                    .size(size)
                    .offset(
                        x = offset.x * 300.dp, y = offset.y * 500.dp
                    )
                    .background(
                        MaterialTheme.colorScheme.tertiary.copy(alpha = alpha), shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun AnimatedGameStatCard(title: String, value: String) {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(value) {
        scale = 1.2f
        delay(50)
        scale = 1f
    }

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .scale(animatedScale),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title, style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                text = value, style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun TimerDisplay(timeLeft: Int) {
    val isLowTime = timeLeft <= 20  // Warning threshold for hard mode
    val color by animateColorAsState(
        targetValue = if (isLowTime) Color.Red else MaterialTheme.colorScheme.primary,
        animationSpec = tween(500)
    )

    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(timeLeft) {
        if (isLowTime) {
            scale = 1.2f
            delay(100)
            scale = 1f
        }
    }

    Text(
        text = timeLeft.toString(), style = MaterialTheme.typography.headlineMedium.copy(
            color = color, fontWeight = FontWeight.Bold
        ), modifier = Modifier.scale(animatedScale)
    )
} 