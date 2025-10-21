package dev.toufikforyou.colormatching.main.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import dev.toufikforyou.colormatching.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(title: String = "Color Maching", onNavigateBack: () -> Unit) {
    TopAppBar(title = {
        Text(
            title, style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold
            )
        )
    }, navigationIcon = {
        IconButton(onClick = onNavigateBack) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent
    )
    )
}