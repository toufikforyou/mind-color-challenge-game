package com.toufikforyou.colormatching.main.utils

import androidx.compose.ui.graphics.Color
import com.toufikforyou.colormatching.main.presentation.components.ColorBox
import kotlin.random.Random

fun generateColorPairs(gridSize: Int): List<ColorBox> {
    val totalBoxes = gridSize * gridSize
    val pairs = (totalBoxes - 1) / 2
    val colors = generateRandomColors(pairs)
    val allColors = (colors + colors).shuffled()
    
    return List(totalBoxes) { index ->
        if (index < allColors.size) {
            ColorBox(color = allColors[index])
        } else {
            ColorBox(color = Color.Gray) // Center box in odd-sized grids
        }
    }
}

private fun generateRandomColors(count: Int): List<Color> {
    return List(count) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )
    }
} 