package com.toufikforyou.colormatching.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.toufikforyou.colormatching.main.presentation.screens.game.EasyGameScreen
import com.toufikforyou.colormatching.main.presentation.screens.game.MediumGameScreen
import com.toufikforyou.colormatching.main.presentation.components.guide.GuideScreen
import com.toufikforyou.colormatching.main.presentation.components.highscores.HighScoresScreen
import com.toufikforyou.colormatching.main.presentation.screens.home.HomeScreen
import com.toufikforyou.colormatching.main.presentation.components.levelselection.LevelSelectionScreen
import com.toufikforyou.colormatching.main.presentation.screens.game.HardGameScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.LevelSelection.route) {
            LevelSelectionScreen(navController)
        }

        composable(Screen.Game.Easy.route) {
            EasyGameScreen(navController)
        }

        composable(Screen.Game.Medium.route) {
            MediumGameScreen(navController)
        }

        composable(Screen.Game.Hard.route) {
            HardGameScreen(navController)
        }

        composable(Screen.Guide.route) {
            GuideScreen(navController)
        }

        composable(Screen.HighScores.route) {
            HighScoresScreen(navController)
        }
    }
} 