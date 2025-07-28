package dev.toufikforyou.colormatching.main.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A composable that observes the lifecycle and calls a callback when the app goes to background.
 * This is useful for pausing timers when the app is not in foreground to maintain accurate
 * screen time reporting and prevent background time accumulation.
 *
 * @param onPause Callback that will be invoked when the ON_PAUSE lifecycle event occurs
 */
@Composable
fun PauseOnBackground(onPause: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                onPause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}