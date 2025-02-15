package com.toufikforyou.colormatching.main.presentation.screens.levelselection

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.toufikforyou.colormatching.main.navigation.Screen
import com.toufikforyou.colormatching.main.presentation.components.GameBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(navController: NavController) {
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
        // Animated background
        GameBackground()

        Scaffold(containerColor = Color.Transparent, topBar = {
            TopAppBar(title = {
                Text(
                    "Select Difficulty",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DifficultyButton(text = "Easy",
                    description = "3x3 Grid",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { navController.navigate(Screen.Game.Easy.route) })

                Spacer(modifier = Modifier.height(24.dp))

                DifficultyButton(text = "Medium",
                    description = "4x4 Grid",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { navController.navigate(Screen.Game.Medium.route) })

                Spacer(modifier = Modifier.height(24.dp))

                DifficultyButton(text = "Hard",
                    description = "5x5 Grid",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = { navController.navigate(Screen.Game.Hard.route) })
            }
        }
    }
}

@Composable
private fun DifficultyButton(
    text: String, description: String, containerColor: Color, onClick: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    Surface(modifier = Modifier
        .width(280.dp)
        .scale(animatedScale),
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        onClick = {
            scale = 0.95f
            onClick()
        }) {
        Column(
            modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text, style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description, style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            )
        }
    }

    LaunchedEffect(scale) {
        if (scale < 1f) {
            delay(100)
            scale = 1f
        }
    }
}