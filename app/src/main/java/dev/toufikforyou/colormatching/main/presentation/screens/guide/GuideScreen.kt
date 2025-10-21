package dev.toufikforyou.colormatching.main.presentation.screens.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.R
import dev.toufikforyou.colormatching.main.presentation.components.GameAppBar
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground

@Composable
fun GuideScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            GameAppBar(
                title = "How to Play"
            ) {
                navController.navigateUp()
            }
        }) { padding ->
        GameBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            GuideSection(title = "Game Modes", icon = R.drawable.build_24px, content = {
                BulletPoint("Easy: 3x3 grid, perfect for beginners")
                BulletPoint("Medium: 4x4 grid, increased challenge")
                BulletPoint("Hard: 5x5 grid, for expert players")
            })

            GuideSection(title = "How to Play", icon = R.drawable.check_circle_24px, content = {
                BulletPoint("Memorize the colors shown initially")
                BulletPoint("Click pairs of boxes to match colors")
                BulletPoint("Match all pairs before time runs out")
                BulletPoint("Quick matches earn bonus points")
            })

            GuideSection(title = "Scoring System", icon = R.drawable.build_24px, content = {
                BulletPoint("Base points for each match: 10")
                BulletPoint("Quick match bonus: +5 to +15")
                BulletPoint("Streak bonus: +5 per match")
                BulletPoint("Time bonus for early completion")
            })

            GuideSection(title = "Tips & Tricks", icon = R.drawable.info_24px, content = {
                BulletPoint("Focus on remembering color patterns")
                BulletPoint("Start with easy mode to practice")
                BulletPoint("Try to maintain matching streaks")
                BulletPoint("Watch the timer for urgency")
            })
        }
    }
}

@Composable
private fun GuideSection(
    title: String, icon: Int, content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title, style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ), color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "•",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 