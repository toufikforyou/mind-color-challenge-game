package dev.toufikforyou.colormatching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.navigation.NavGraph
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.ui.theme.ColorMatchingTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val preferencesDataStore: PreferencesDataStore by inject()
    private val soundManager: SoundManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by preferencesDataStore.isDarkMode.collectAsState(initial = true)
            val useSystemTheme by preferencesDataStore.useSystemTheme.collectAsState(initial = true)
            val isSystemInDarkTheme = isSystemInDarkTheme()

            val shouldUseDarkTheme = if (useSystemTheme) {
                isSystemInDarkTheme
            } else {
                isDarkMode
            }

            ColorMatchingTheme(darkTheme = shouldUseDarkTheme) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    preferencesDataStore = preferencesDataStore,
                    soundManager = soundManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}