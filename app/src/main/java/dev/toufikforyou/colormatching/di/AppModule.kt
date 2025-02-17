package dev.toufikforyou.colormatching.di

import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.data.local.GameDatabase
import dev.toufikforyou.colormatching.main.utils.SoundManager
import dev.toufikforyou.colormatching.main.presentation.viewmodels.GameViewModel
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
            preferencesDataStore = get(),
            gameProgressDao = get(),
            initialGridSize = gridSize,
            difficulty = difficulty
        )
    }
} 