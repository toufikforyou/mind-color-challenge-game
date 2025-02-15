package com.toufikforyou.colormatching.main.presentation.components.guide

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guide") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = """
                    How to Play:
                    
                    1. Click 'Play Game' to start
                    2. You'll see 9 boxes in a 3x3 grid
                    3. Initially, colors will be shown for 5 seconds
                    4. Remember the positions of matching colors
                    5. Click pairs of boxes to match colors
                    6. Match all pairs within 20 seconds
                    7. Each correct match gives you 10 points
                    
                    Good luck!
                """.trimIndent(),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
} 