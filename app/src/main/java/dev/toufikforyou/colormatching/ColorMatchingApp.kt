package dev.toufikforyou.colormatching

import android.app.Application
import dev.toufikforyou.colormatching.main.di.appModule
import dev.toufikforyou.colormatching.main.di.dataModule
import dev.toufikforyou.colormatching.main.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ColorMatchingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ColorMatchingApp)
            modules(listOf(appModule, dataModule, viewModelModule))
        }
    }
} 