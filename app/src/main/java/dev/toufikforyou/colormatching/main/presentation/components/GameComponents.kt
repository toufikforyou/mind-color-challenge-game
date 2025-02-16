package dev.toufikforyou.colormatching.main.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameOverDialog(
    score: Int, matchedPairs: Int, totalPairs: Int, onTryAgain: () -> Unit, onBack: () -> Unit
) {
    AlertDialog(onDismissRequest = { }, title = { Text("Game Over!") }, text = {
        Column {
            Text("Time's up! Your score: $score")
            Text("Matched pairs: $matchedPairs/$totalPairs")
        }
    }, confirmButton = {
        Button(onClick = onTryAgain) {
            Text("Try Again")
        }
    }, dismissButton = {
        Button(onClick = onBack) {
            Text("Back to Menu")
        }
    })
}