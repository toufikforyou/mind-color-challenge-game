package com.toufikforyou.colormatching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.toufikforyou.colormatching.main.navigation.NavGraph
import com.toufikforyou.colormatching.ui.theme.ColorMatchingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorMatchingTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}