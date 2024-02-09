package com.example.neptune.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = White,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    inversePrimary = Blue40,

    secondary = DarkBlue80,
    onSecondary = DarkBlue20,
    secondaryContainer = DarkBlue30,
    onSecondaryContainer = DarkBlue90,

    background = PrimaryBackgroundColor,
    onBackground = DarkBlue90,
    surface = DarkBlue80,
    onSurface = DarkBlue90,
    tertiary = ButtonBlue
)

/**
 * The Theme for the NeptuneApp. The content of each View is wrapped by this theme.
 * The color scheme of the theme is always set to the DarkColorScheme.
 * @param content The Composable which is wrapped by the NeptuneTheme
 */
@Composable
fun NeptuneTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val darkTheme = true

    val view = LocalView.current
    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}