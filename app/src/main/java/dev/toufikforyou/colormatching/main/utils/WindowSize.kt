package dev.toufikforyou.colormatching.main.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSize { Compact, Medium, Expanded }

@Composable
fun rememberWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    return remember(screenWidth) {
        when {
            screenWidth < 600.dp -> WindowSize.Compact
            screenWidth < 840.dp -> WindowSize.Medium
            else -> WindowSize.Expanded
        }
    }
}
