package dev.toufikforyou.colormatching.main.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object LevelSelection : Screen("level_selection")
    object Settings : Screen("settings")
    object Guide : Screen("guide")
    object HighScores : Screen("high_scores")
    
    sealed class Game(route: String) : Screen(route) {
        object Easy : Game("game/easy")
        object Medium : Game("game/medium")
        object Hard : Game("game/hard")
    }
}