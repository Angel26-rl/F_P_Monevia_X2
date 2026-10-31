package com.angel.monevia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF35D39A),
    onPrimary = Color(0xFF101412),

    secondary = Color(0xFF173D2D),
    onSecondary = Color(0xFFF1F5F2),

    tertiary = Color(0xFFADB9B1),
    onTertiary = Color(0xFF101412),

    background = Color(0xFF101412),
    onBackground = Color(0xFFF1F5F2),

    surface = Color(0xFF17201B),
    onSurface = Color(0xFFF1F5F2),

    surfaceVariant = Color(0xFF223129),
    onSurfaceVariant = Color(0xFFADB9B1),

    error = Color(0xFFFF6B72),
    onError = Color(0xFF101412)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF176B4A),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFF35D39A),
    onSecondary = Color(0xFF101412),

    tertiary = Color(0xFF527461),
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFFF1F5F2),
    onBackground = Color(0xFF101412),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF101412),

    surfaceVariant = Color(0xFFE1EAE4),
    onSurfaceVariant = Color(0xFF526158),

    error = Color(0xFFD85C5C),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun MoneviaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
