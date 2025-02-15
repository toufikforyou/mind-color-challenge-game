package com.toufikforyou.colormatching.main.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toufikforyou.colormatching.main.presentation.ui.theme.GameColors
import kotlinx.coroutines.delay

@Composable
fun ColorGrid(
    gridSize: Int, colorBoxes: List<ColorBox>, showInitialColors: Boolean, onBoxClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(colorBoxes.size) { index ->
            val box = colorBoxes[index]
            ColorBox(
                color = if (showInitialColors || box.isSelected || box.isMatched) box.color
                else GameColors.CardBackground,
                isMatched = box.isMatched,
                onClick = { onBoxClick(index) },
                modifier = Modifier
                    .aspectRatio(1f)
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun ColorBox(
    color: Color,
    isMatched: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(isMatched) {
        if (isMatched) {
            scale = 1.2f
            delay(100)
            scale = 1f
        }
    }

    Surface(
        modifier = modifier
            .scale(animatedScale)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        color = color,
        border = if (isMatched) BorderStroke(2.dp, GameColors.AccentColor) else null,
        onClick = onClick
    ) {
        if (isMatched) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = GameColors.TextPrimary,
                    modifier = Modifier.scale(animatedScale)
                )
            }
        }
    }
} 