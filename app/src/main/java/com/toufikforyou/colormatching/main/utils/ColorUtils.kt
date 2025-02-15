package com.toufikforyou.colormatching.main.utils

import androidx.compose.ui.graphics.Color
import com.toufikforyou.colormatching.main.presentation.components.ColorBox
import kotlin.random.Random

fun generateColorPairs(gridSize: Int): List<ColorBox> {
    val totalBoxes = gridSize * gridSize
    val pairs = (totalBoxes - 1) / 2
    val colors = generateRandomColors(pairs)
    val allColors = (colors + colors).shuffled()

    // Randomly select which position to ignore
    val ignoredPosition = Random.nextInt(totalBoxes)

    return List(totalBoxes) { index ->
        if (index == ignoredPosition) {
            ColorBox(color = Color.Gray) // Randomly ignored box
        } else {
            // Calculate the correct color index accounting for the ignored position
            val colorIndex = if (index < ignoredPosition) index else index - 1
            ColorBox(color = allColors[colorIndex])
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