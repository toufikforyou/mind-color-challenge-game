package dev.toufikforyou.colormatching.main.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object LevelSelection : Screen("level_selection")
    data object Settings : Screen("settings")
    data object Guide : Screen("guide")
    data object HighScores : Screen("high_scores")

    sealed class Game(route: String) : Screen(route) {
        data object Easy : Game("game/easy")
        data object Medium : Game("game/medium")
        data object Hard : Game("game/hard")
    }
}