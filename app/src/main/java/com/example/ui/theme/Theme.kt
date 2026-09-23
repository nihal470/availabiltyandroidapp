package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TravzRedPrimary,
    onPrimary = TravzWhite,
    primaryContainer = TravzRedDark,
    onPrimaryContainer = TravzRedLight,
    secondary = TravzWhite,
    onSecondary = TravzBlack,
    background = TravzBlack,
    onBackground = TravzWhite,
    surface = TravzDarkSurface,
    onSurface = TravzWhite,
    surfaceVariant = TravzDarkCard,
    onSurfaceVariant = TravzWhite,
    outline = TravzDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = TravzRedPrimary,
    onPrimary = TravzWhite,
    primaryContainer = TravzRedLight,
    onPrimaryContainer = TravzRedDark,
    secondary = TravzBlack,
    onSecondary = TravzWhite,
    background = TravzOffWhite,
    onBackground = TravzTextPrimary,
    surface = TravzWhite,
    onSurface = TravzTextPrimary,
    surfaceVariant = TravzLightSurface,
    onSurfaceVariant = TravzTextSecondary,
    outline = TravzBorderLight
)

@Composable
fun TravzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally keep the brand cohesive: Travz black, white, and red
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    TravzTheme(darkTheme = darkTheme, content = content)
}
