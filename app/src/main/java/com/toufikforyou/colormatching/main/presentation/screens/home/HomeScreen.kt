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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.toufikforyou.colormatching.main.navigation.Screen
import com.toufikforyou.colormatching.main.presentation.components.GameBackground
import com.toufikforyou.colormatching.main.utils.SoundManager
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController, soundManager: SoundManager, isSoundEnabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.58f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        GameBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedLogo()
            MenuList(navController, soundManager, isSoundEnabled)
        }
    }
}

@Composable
private fun AnimatedLogo() {
    val scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale, animationSpec = infiniteRepeatable(
            animation = tween(2000), repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Color", style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 72.sp
            ), modifier = Modifier.scale(animatedScale)
        )
        Text(
            text = "Match", style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp
            ), modifier = Modifier.scale(animatedScale)
        )
    }
}

@Composable
private fun MenuList(
    navController: NavController, soundManager: SoundManager, isSoundEnabled: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MenuButton(
            text = "Play Game",
            icon = Icons.Filled.PlayArrow,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            if (isSoundEnabled) soundManager.playButtonClick()
            navController.navigate(Screen.LevelSelection.route)
        }

        MenuButton(
            text = "High Scores",
            icon = Icons.Filled.Info,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            if (isSoundEnabled) soundManager.playButtonClick()
            navController.navigate(Screen.HighScores.route)
        }

        MenuButton(
            text = "Settings",
            icon = Icons.Filled.Settings,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            if (isSoundEnabled) soundManager.playButtonClick()
            navController.navigate(Screen.Settings.route)
        }

        MenuButton(
            text = "How to Play",
            icon = Icons.Filled.Info,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            if (isSoundEnabled) soundManager.playButtonClick()
            navController.navigate(Screen.Guide.route)
        }
    }
}

@Composable
private fun MenuButton(
    text: String, icon: ImageVector, containerColor: Color, onClick: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
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
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text, style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold
                )
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
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