package dev.toufikforyou.colormatching.main.di

import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.data.local.GameDatabase
import dev.toufikforyou.colormatching.main.presentation.viewmodels.GameViewModel
import dev.toufikforyou.colormatching.main.presentation.viewmodels.HighScoresViewModel
import dev.toufikforyou.colormatching.main.utils.SoundManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SoundManager(androidContext()) }
    single { PreferencesDataStore(androidContext()) }
    single { GameDatabase.getDatabase(androidContext()) }
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
            initialGridSize = gridSize,
            difficulty = difficulty
        )
    }
    
    viewModel {
        HighScoresViewModel(
            highScoreDao = get()
        )
    }
} 