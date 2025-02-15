package com.toufikforyou.colormatching.main.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Guide : Screen("guide")
    object HighScores : Screen("high_scores")
    object LevelSelection : Screen("level_selection")
    
    sealed class Game(route: String) : Screen(route) {
        object Easy : Game("game/easy")
        object Medium : Game("game/medium")
        object Hard : Game("game/hard")
    }
}