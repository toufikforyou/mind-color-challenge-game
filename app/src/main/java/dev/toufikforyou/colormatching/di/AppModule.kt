package dev.toufikforyou.colormatching.di

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
}

val viewModelModule = module {
    viewModel { (gridSize: Int, difficulty: String) ->
        GameViewModel(
            gameProgressDao = get(),
            initialGridSize = gridSize,
            difficulty = difficulty
        )
    }
    
    viewModel {
        HighScoresViewModel(
            preferencesDataStore = get()
        )
    }
} 