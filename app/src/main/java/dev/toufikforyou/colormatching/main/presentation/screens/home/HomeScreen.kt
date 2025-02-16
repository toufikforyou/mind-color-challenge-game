@file:Suppress("SameParameterValue")

package dev.toufikforyou.colormatching.main.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.BuildConfig
import dev.toufikforyou.colormatching.main.navigation.Screen
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground
import dev.toufikforyou.colormatching.main.utils.SoundManager

@Composable
fun HomeScreen(
    navController: NavController, soundManager: SoundManager, isSoundEnabled: Boolean
) {
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            GameBackground()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                AnimatedLogo()

                Spacer(modifier = Modifier.height(48.dp))

                // Start Game Button (Highlighted)
                LargeMenuButton(title = "Start Game",
                    subtitle = "Begin your color matching journey",
                    icon = Icons.Filled.PlayArrow,
                    onClick = {
                        if (isSoundEnabled) soundManager.playButtonClick()
                        navController.navigate(Screen.LevelSelection.route)
                    })

                Spacer(modifier = Modifier.height(32.dp))

                // Other Menu Items in a Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SmallMenuButton(icon = Icons.Filled.MailOutline, label = "Scores", onClick = {
                        if (isSoundEnabled) soundManager.playButtonClick()
                        navController.navigate(Screen.HighScores.route)
                    })

                    SmallMenuButton(icon = Icons.Filled.Settings, label = "Settings", onClick = {
                        if (isSoundEnabled) soundManager.playButtonClick()
                        navController.navigate(Screen.Settings.route)
                    })

                    SmallMenuButton(icon = Icons.Filled.Info, label = "Guide", onClick = {
                        if (isSoundEnabled) soundManager.playButtonClick()
                        navController.navigate(Screen.Guide.route)
                    })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "v${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AnimatedLogo() {
    var animationState by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (animationState) 360f else 0f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (animationState) 1.2f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium
        )
    )

    LaunchedEffect(Unit) {
        animationState = true
    }

    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    val gradientColors = remember(primary, secondary) {
        listOf(primary, secondary)
    }

    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(scale)
            .clip(MaterialTheme.shapes.large),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(colors = gradientColors), size = size
            )
        }

        Text(
            text = "🎨",
            modifier = Modifier
                .size(120.dp)
                .rotate(rotation),
            style = MaterialTheme.typography.displayLarge,
            fontSize = 80.sp
        )
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun LargeMenuButton(
    title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    var isPulsing by remember { mutableStateOf(false) }

    // Start the continuous pulse animation
    LaunchedEffect(Unit) {
        isPulsing = true
    }

    // Continuous pulse animation
    val pulseScale by animateFloatAsState(
        targetValue = if (isPulsing) 1.05f else 1f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )

    // Button scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    // Continuous floating animation
    val floatOffset by animateFloatAsState(
        targetValue = if (isHovered) -8f else 0f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )

    // Glow animation
    val buttonGlow by animateFloatAsState(
        targetValue = if (isPressed) 0f else if (isHovered) 1f else 0.5f, animationSpec = tween(500)
    )

    // Icon rotation and scale
    val iconScale by animateFloatAsState(
        targetValue = if (isHovered) 1.2f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isPressed) 360f else if (isHovered) 20f else 0f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    // Add continuous icon pulse
    val iconPulse by animateFloatAsState(
        targetValue = if (isPulsing) 1.1f else 1f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(500) // Offset to create alternating effect with button
        )
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp, vertical = 8.dp)
        .height(120.dp)
        .scale(scale * pulseScale)
        .offset(y = floatOffset.dp)
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
        // Background glow effect
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-4).dp)
                .alpha(buttonGlow),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) { }

        // Main button
        Card(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    })
                }, shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ), elevation = CardDefaults.cardElevation(
                defaultElevation = if (isPressed) 0.dp else 8.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title, style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ), color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .scale(iconScale * iconPulse)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = if (isHovered) 0.2f else 0.1f
                            )
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(iconRotation),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun SmallMenuButton(
    icon: ImageVector, label: String, onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier
            .size(100.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    onClick()
                })
            }, shape = MaterialTheme.shapes.medium, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 0.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}