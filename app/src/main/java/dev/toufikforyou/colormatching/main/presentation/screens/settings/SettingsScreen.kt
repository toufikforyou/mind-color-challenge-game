package dev.toufikforyou.colormatching.main.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.toufikforyou.colormatching.main.presentation.components.GameAppBar
import dev.toufikforyou.colormatching.main.presentation.components.GameBackground

@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    isSoundEnabled: Boolean,
    useSystemTheme: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    onSoundEnabledChanged: (Boolean) -> Unit,
    onUseSystemThemeChanged: (Boolean) -> Unit
) {
    Scaffold(modifier = Modifier
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
                icon = if (useSystemTheme) Icons.Default.CheckCircle else Icons.Filled.CheckCircle,
                isChecked = useSystemTheme,
                onCheckedChange = onUseSystemThemeChanged
            )

            if (!useSystemTheme) {
                SettingsItem(
                    title = "Dark Mode",
                    icon = if (isDarkMode) Icons.Default.CheckCircle else Icons.Filled.CheckCircle,
                    isChecked = isDarkMode,
                    onCheckedChange = onDarkModeChanged
                )
            }

            SettingsItem(
                title = "Sound Effects",
                icon = if (isSoundEnabled) Icons.Default.Add else Icons.Default.AddCircle,
                isChecked = isSoundEnabled,
                onCheckedChange = onSoundEnabledChanged
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String, icon: ImageVector, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit
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
                    imageVector = icon,
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