package dev.toufikforyou.colormatching.main.presentation.screens.highscores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighScoresScreen(navController: NavController) {
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = {
                Text(
                    "High Scores", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
            )
        }) { padding ->
        GameBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Top Scores List
            ScoresList()
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ScoresList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(10) { index ->
            ScoreItem(
                rank = index + 1,
                score = (1000 - (index * 50)).toString(),
                date = "2024-02-${20 - index}"
            )
        }
    }
}

@Composable
private fun ScoreItem(rank: Int, score: String, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rank Circle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (rank) {
                                1 -> MaterialTheme.colorScheme.primary
                                2 -> MaterialTheme.colorScheme.secondary
                                3 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        ), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$rank",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Column {
                    Text(
                        text = score, style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ), color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = when (rank) {
                    1 -> Color(0xFF1DDA25)
                    2 -> Color(0xFFC77F37)
                    3 -> Color(0xFF2196F3)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
} 