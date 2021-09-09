package ru.awawa.clockutils.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Grey700,
    primaryVariant = Grey700,
    secondary = Teal400,
    secondaryVariant = Teal500,
    background = Grey600,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = Grey700,
    primaryVariant = Grey700,
    secondary = Teal400,
    secondaryVariant = Teal500,
    background = Grey600,
    onBackground = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ClockUtilsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}