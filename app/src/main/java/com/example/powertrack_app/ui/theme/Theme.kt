package com.example.powertrack_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PowerTrackColorScheme = darkColorScheme(
    primary = PowerTrackBlueLight,
    onPrimary = PowerTrackOnPrimary,
    primaryContainer = PowerTrackBlueMedium,
    onPrimaryContainer = PowerTrackOnPrimary,
    secondary = PowerTrackAccent,
    onSecondary = PowerTrackOnPrimary,
    background = PowerTrackSurface,
    onBackground = PowerTrackOnSurface,
    surface = PowerTrackSurface,
    onSurface = PowerTrackOnSurface,
    onSurfaceVariant = PowerTrackGrey,
    error = PowerTrackError,
    onError = PowerTrackOnPrimary,
)

@Composable
fun PowerTrackTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PowerTrackColorScheme,
        typography = Typography,
        content = content
    )
}