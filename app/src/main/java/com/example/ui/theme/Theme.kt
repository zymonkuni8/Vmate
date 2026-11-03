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
    primary = Indigo400,
    onPrimary = DarkBackground,
    primaryContainer = Indigo600,
    onPrimaryContainer = Indigo100,
    secondary = Lavender400,
    onSecondary = DarkBackground,
    secondaryContainer = Lavender500,
    onSecondaryContainer = Lavender50,
    tertiary = Cyan400,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardClay,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorderGlass
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = LightSurface,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo600,
    secondary = Lavender500,
    onSecondary = LightSurface,
    secondaryContainer = Lavender200,
    onSecondaryContainer = LightTextPrimary,
    tertiary = Cyan500,
    onTertiary = LightSurface,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCardClay,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorderGlass
)

@Composable
fun VaishAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve custom claymorphic branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
