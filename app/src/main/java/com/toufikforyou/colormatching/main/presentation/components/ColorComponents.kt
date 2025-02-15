package com.toufikforyou.colormatching.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ColorBox(
    val color: Color,
    val isSelected: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
fun ColorBox(
    box: ColorBox,
    showInitialColors: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                when {
                    box.isMatched -> box.color
                    box.isSelected -> box.color
                    showInitialColors -> box.color
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .clickable(enabled = !box.isMatched, onClick = onClick)
    )
}