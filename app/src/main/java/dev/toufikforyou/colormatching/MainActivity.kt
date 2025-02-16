package dev.toufikforyou.colormatching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.navigation.NavGraph
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.ui.theme.ColorMatchingTheme

class MainActivity : ComponentActivity() {
    private lateinit var preferencesDataStore: PreferencesDataStore
    private lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesDataStore = PreferencesDataStore(this)
        soundManager = SoundManager(this)

        setContent {
            val isDarkMode by preferencesDataStore.isDarkMode.collectAsState(initial = true)

            ColorMatchingTheme(darkTheme = isDarkMode) {
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