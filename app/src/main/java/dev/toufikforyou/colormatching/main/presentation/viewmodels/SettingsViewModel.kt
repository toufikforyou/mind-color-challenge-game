package dev.toufikforyou.colormatching.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.toufikforyou.colormatching.main.data.PreferencesDataStore
import dev.toufikforyou.colormatching.main.notification.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesDataStore: PreferencesDataStore,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    val isDarkMode = preferencesDataStore.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val isSoundEnabled = preferencesDataStore.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val useSystemTheme = preferencesDataStore.useSystemTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.updateDarkMode(enabled)
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.updateSoundEnabled(enabled)
        }
    }

    fun updateUseSystemTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.updateUseSystemTheme(enabled)
        }
    }

    fun openNotificationSettings() {
        notificationHelper.openNotificationSettings()
    }
} 