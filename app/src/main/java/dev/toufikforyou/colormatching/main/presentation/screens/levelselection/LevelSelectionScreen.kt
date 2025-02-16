package dev.toufikforyou.colormatching.main.presentation.screens.levelselection

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.main.navigation.Screen
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(navController: NavController) {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            MaterialTheme.colorScheme.background
        ), topBar = {
        TopAppBar(title = {
            Text(
                "Select Difficulty", style = MaterialTheme.typography.titleLarge.copy(
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

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun DifficultyButton(
    text: String, description: String, containerColor: Color, onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    // Button scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    // Floating animation
    val floatOffset by animateFloatAsState(
        targetValue = if (isHovered) -8f else 0f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )

    // Glow animation
    val buttonGlow by animateFloatAsState(
        targetValue = if (isPressed) 0f else if (isHovered) 1f else 0.5f, animationSpec = tween(500)
    )

    // Text scale animation
    val textScale by animateFloatAsState(
        targetValue = if (isHovered) 1.1f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    Box(modifier = Modifier
        .width(280.dp)
        .offset(y = floatOffset.dp)
        .scale(scale)
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type) {
                        PointerEventType.Enter -> isHovered = true
                        PointerEventType.Exit -> isHovered = false
                        else -> { /* do nothing */
                        }
                    }
                }
            }
        }) {
        // Glow effect
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(buttonGlow),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        ) {
            Spacer(modifier = Modifier.height(88.dp))
        }

        // Main button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    })
                },
            shape = RoundedCornerShape(24.dp),
            color = containerColor,
            shadowElevation = if (isPressed) 0.dp else 8.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.scale(textScale),
                        style = MaterialTheme.typography.headlineLarge.copy(
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

                // Animated icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = if (isHovered) 0.2f else 0.1f
                            )
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .scale(if (isHovered) 1.2f else 1f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}