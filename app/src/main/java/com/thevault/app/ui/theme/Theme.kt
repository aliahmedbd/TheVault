package com.thevault.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF004D64),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF006684),
    secondary = Color(0xFF006972),
    onSecondary = Color.White,
    background = Color(0xFFF8F9FF),
    surface = Color(0xFFF8F9FF),
    onSurface = Color(0xFF171C22),
    surfaceVariant = Color(0xFFDEE3EB),
    onSurfaceVariant = Color(0xFF3F484D),
    outline = Color(0xFF70787E)
)

@Composable
fun TheVaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) LightColorScheme else LightColorScheme // Forcing light for now to match design

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
