package com.toufikforyou.colormatching.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.toufikforyou.colormatching.main.presentation.animation.rememberFloatingParticle
import kotlin.random.Random

@Composable
fun GameBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(20.dp)
    ) {
        repeat(20) {
            val offset = rememberFloatingParticle()
            val size = remember { Random.nextInt(8, 17).dp }
            val alpha = remember { Random.nextFloat() * (0.15f - 0.05f) + 0.05f }

            Box(
                modifier = Modifier
                    .size(size)
                    .offset(
                        x = offset.x * 300.dp,
                        y = offset.y * 500.dp
                    )
                    .background(
                        MaterialTheme.colorScheme.tertiary.copy(alpha = alpha),
                        shape = CircleShape
                    )
            )
        }
    }
} 