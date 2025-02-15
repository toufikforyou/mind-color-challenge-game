package com.toufikforyou.colormatching.main.presentation.screens.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.toufikforyou.colormatching.main.navigation.Screen
import com.toufikforyou.colormatching.main.presentation.components.GameBackground
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
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
        // Animated background particles
        GameBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Game Title
            GameTitle()

            // Menu Buttons
            MenuButtons(navController)
        }
    }
}

@Composable
private fun GameTitle() {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = infiniteRepeatable(
            animation = tween(2000), repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        scale = 1.1f
    }

    Text(
        text = "Color Match", style = MaterialTheme.typography.displayLarge.copy(
            color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
        ), modifier = Modifier.scale(animatedScale)
    )
}

@Composable
private fun MenuButtons(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MenuButton(
            text = "Play Game",
            onClick = { navController.navigate(Screen.LevelSelection.route) })
        MenuButton(
            text = "High Scores",
            onClick = { navController.navigate(Screen.HighScores.route) })
        MenuButton(text = "How to Play", onClick = { navController.navigate(Screen.Guide.route) })
    }
}

@Composable
private fun MenuButton(
    text: String, onClick: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    Surface(modifier = Modifier
        .width(250.dp)
        .scale(animatedScale),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        onClick = {
            scale = 0.95f
            onClick()
        }) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold
            ),
            fontSize = 24.sp
        )
    }

    LaunchedEffect(scale) {
        if (scale < 1f) {
            delay(100)
            scale = 1f
        }
    }
}