package dev.toufikforyou.colormatching.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.presentation.screens.game.EasyGameScreen
import dev.toufikforyou.colormatching.main.presentation.screens.game.HardGameScreen
import dev.toufikforyou.colormatching.main.presentation.screens.game.MediumGameScreen
import dev.toufikforyou.colormatching.main.presentation.screens.guide.GuideScreen
import dev.toufikforyou.colormatching.main.presentation.screens.highscores.HighScoresScreen
import dev.toufikforyou.colormatching.main.presentation.screens.home.HomeScreen
import dev.toufikforyou.colormatching.main.presentation.screens.levelselection.LevelSelectionScreen
import dev.toufikforyou.colormatching.main.presentation.screens.settings.SettingsScreen
import dev.toufikforyou.colormatching.main.utils.SoundManager
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController,
    preferencesDataStore: PreferencesDataStore,
    soundManager: SoundManager
) {
    val isDarkMode by preferencesDataStore.isDarkMode.collectAsState(initial = true)
    val isSoundEnabled by preferencesDataStore.isSoundEnabled.collectAsState(initial = true)
    val useSystemTheme by preferencesDataStore.useSystemTheme.collectAsState(initial = true)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                soundManager = soundManager,
                isSoundEnabled = isSoundEnabled
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController,
                isDarkMode = isDarkMode,
                isSoundEnabled = isSoundEnabled,
                useSystemTheme = useSystemTheme,
                onDarkModeChanged = {
                    scope.launch { preferencesDataStore.updateDarkMode(it) }
                },
                onSoundEnabledChanged = {
                    scope.launch { preferencesDataStore.updateSoundEnabled(it) }
                },
                onUseSystemThemeChanged = {
                    scope.launch { preferencesDataStore.updateUseSystemTheme(it) }
                })
        }

        composable(Screen.LevelSelection.route) {
            LevelSelectionScreen(navController)
        }

        composable(Screen.Game.Easy.route) {
            EasyGameScreen(
                navController = navController,
                soundManager = soundManager,
                isSoundEnabled = isSoundEnabled
            )
        }

        composable(Screen.Game.Medium.route) {
            MediumGameScreen(
                navController = navController,
                soundManager = soundManager,
                isSoundEnabled = isSoundEnabled
            )
        }

        composable(Screen.Game.Hard.route) {
            HardGameScreen(
                navController = navController,
                soundManager = soundManager,
                isSoundEnabled = isSoundEnabled
            )
        }

        composable(Screen.Guide.route) {
            GuideScreen(navController)
        }

        composable(Screen.HighScores.route) {
            HighScoresScreen(navController = navController)
        }
    }
} 