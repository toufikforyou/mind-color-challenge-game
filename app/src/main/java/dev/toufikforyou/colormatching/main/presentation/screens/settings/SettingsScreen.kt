@file:Suppress("SameParameterValue")

package dev.toufikforyou.colormatching.main.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.R
import dev.toufikforyou.colormatching.main.presentation.components.GameAppBar
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground
import dev.toufikforyou.colormatching.main.presentation.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavController, viewModel: SettingsViewModel = koinViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState()
    val useSystemTheme by viewModel.useSystemTheme.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            GameAppBar(title = "Settings") {
                navController.navigateUp()
            }
        }) { padding ->
        GameBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsItem(
                title = "Use System Theme",
                icon = if (useSystemTheme) R.drawable.check_circle_unread_24px else R.drawable.check_circle_24px,
                isChecked = useSystemTheme,
                onCheckedChange = viewModel::updateUseSystemTheme
            )

            if (!useSystemTheme) {
                SettingsItem(
                    title = "Dark Mode",
                    icon = if (isDarkMode) R.drawable.check_circle_unread_24px else R.drawable.check_circle_24px,
                    isChecked = isDarkMode,
                    onCheckedChange = viewModel::updateDarkMode
                )
            }

            SettingsItem(
                title = "Sound Effects",
                icon = if (isSoundEnabled) R.drawable.add_24px else R.drawable.add_circle_24px,
                isChecked = isSoundEnabled,
                onCheckedChange = viewModel::updateSoundEnabled
            )

            SettingsItem(
                title = "Notification Settings",
                subtitle = "Configure daily reminder notifications",
                icon = R.drawable.notification_sound_24px,
                onClick = viewModel::openNotificationSettings
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String, subtitle: String, icon: Int, onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String, icon: Int, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
} 