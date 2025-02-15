package com.toufikforyou.colormatching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.toufikforyou.colormatching.main.data.PreferencesDataStore
import com.toufikforyou.colormatching.main.navigation.NavGraph
import com.toufikforyou.colormatching.main.utils.SoundManager
import com.toufikforyou.colormatching.ui.theme.ColorMatchingTheme

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