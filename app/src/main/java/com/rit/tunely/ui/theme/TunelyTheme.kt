package com.rit.tunely.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/


val PastelGreen = Color(0xFFAEFFD0)
val PastelYellow = Color(0xFFFFFC9C)
val PastelGray = Color(0xFFD3D3D3)
val BackgroundGray = Color(0xFFBDBDBD)
val BorderGray = Color(0xFFBDBDBD)
val PastelRed = Color(0xFFFFD1D1)
val Purple200 = Color(0xFFBB86FC)
val Gold = Color(0xFFFFD54F)
val Silver = Color(0xFFE0E0E0)
val Bronze = Color(0xFFCD7F32)

val md_theme_light_primary = Color(0xFF6750A4)
val md_theme_light_onPrimary = Color.White
val md_theme_light_primaryContainer = Color(0xFFEADDFF)

val md_theme_dark_primary = Color(0xFFD0BCFF)
val md_theme_dark_onPrimary = Color(0xFF381E72)
val md_theme_dark_primaryContainer = Color(0xFF4F378B)

val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer
)
val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer
)

@Composable
fun TunelyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = androidx.compose.material3.Typography(),
        shapes = androidx.compose.material3.Shapes(),
        content = content
    )
}