package com.toufikforyou.colormatching.main.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun GameOverDialog(
    score: Int,
    matchedPairs: Int,
    totalPairs: Int,
    onTryAgain: () -> Unit,
    onBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Game Over!") },
        text = { 
            Column {
                Text("Time's up! Your score: $score")
                Text("Matched pairs: $matchedPairs/$totalPairs")
            }
        },
        confirmButton = {
            Button(onClick = onTryAgain) {
                Text("Try Again")
            }
        },
        dismissButton = {
            Button(onClick = onBack) {
                Text("Back to Menu")
            }
        }
    )
}

@Composable
fun GameStatCard(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LevelUpDialog(
    score: Int,
    currentLevel: Int,
    onNextLevel: () -> Unit,
    onBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Level Complete!") },
        text = { 
            Column {
                Text("Congratulations! Level $currentLevel completed!")
                Text("Your score: $score")
                Text("Ready for the next level?")
            }
        },
        confirmButton = {
            Button(onClick = onNextLevel) {
                Text("Next Level")
            }
        },
        dismissButton = {
            Button(onClick = onBack) {
                Text("Back to Menu")
            }
        }
    )
}

@Composable
fun WinDialog(
    score: Int,
    onNextLevel: (() -> Unit)?,
    onBack: () -> Unit,
    showNextLevelButton: Boolean = false,
    message: String = "Congratulations! You won!"
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Victory!") },
        text = { 
            Column {
                Text(message)
                Text("Final Score: $score")
            }
        },
        confirmButton = {
            if (showNextLevelButton && onNextLevel != null) {
                Button(onClick = onNextLevel) {
                    Text("Next Difficulty")
                }
            } else {
                Button(onClick = onBack) {
                    Text("Back to Menu")
                }
            }
        },
        dismissButton = {
            Button(onClick = onBack) {
                Text("Back to Menu")
            }
        }
    )
} 