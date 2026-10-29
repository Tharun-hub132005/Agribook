package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AgriDarkColorScheme = darkColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = AgriBlack,
    primaryContainer = AgriGreenContainer,
    onPrimaryContainer = AgriOnGreenContainer,
    secondary = AgriLime,
    onSecondary = AgriBlack,
    secondaryContainer = AgriGreenContainer,
    onSecondaryContainer = AgriLime,
    tertiary = AgriGold,
    onTertiary = AgriBlack,
    tertiaryContainer = AgriGoldContainer,
    onTertiaryContainer = AgriGold,
    background = AgriBlack,
    onBackground = AgriTextPrimary,
    surface = AgriSurfaceDark,
    onSurface = AgriTextPrimary,
    surfaceVariant = AgriSurfaceCard,
    onSurfaceVariant = AgriTextSecondary,
    outline = AgriBorder,
    error = AgriRedAlert,
    onError = AgriTextPrimary,
    errorContainer = AgriRedContainer,
    onErrorContainer = AgriRedAlert
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AgriDarkColorScheme,
        typography = Typography,
        content = content
    )
}
