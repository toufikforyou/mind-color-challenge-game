package dev.toufikforyou.colormatching.main.di

import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.data.local.GameDatabase
import dev.toufikforyou.colormatching.main.presentation.viewmodels.GameViewModel
import dev.toufikforyou.colormatching.main.presentation.viewmodels.HighScoresViewModel
import dev.toufikforyou.colormatching.main.presentation.viewmodels.SettingsViewModel
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.main.notification.NotificationHelper
import dev.toufikforyou.colormatching.main.notification.NotificationPermissionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SoundManager(androidContext()) }
    single { PreferencesDataStore(androidContext()) }
    single { GameDatabase.getDatabase(androidContext()) }
    single { NotificationPermissionHandler(androidContext()) }
    single { NotificationHelper(androidContext(), get()) }
}

val dataModule = module {
    single { get<GameDatabase>().gameProgressDao() }
    single { get<GameDatabase>().highScoreDao() }
}

val viewModelModule = module {
    viewModel { (gridSize: Int, difficulty: String) ->
        GameViewModel(
            gameProgressDao = get(),
            highScoreDao = get(),
            notificationHelper = get(),
            initialGridSize = gridSize,
            difficulty = difficulty
        )
    }
    
    viewModel {
        HighScoresViewModel(
            highScoreDao = get()
        )
    }

    viewModel {
        SettingsViewModel(
            preferencesDataStore = get(),
            notificationHelper = get()
        )
    }
} 