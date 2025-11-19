package com.kittel.ultraminimallauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,        // Farbe für wichtige Buttons
    background = White,        // Haupt-Hintergrundfarbe
    surface = LightBlue,       // Hintergrundfarbe für Karten/Listenelemente
    onPrimary = White,         // Textfarbe auf primären Buttons
    onBackground = DarkBlue,   // Haupt-Textfarbe
    onSurface = DarkBlue       // Textfarbe auf Karten
)


// DAS IST DIE ENTSCHEIDENDE FARBPALETTE
private val DarkColorScheme = lightColorScheme(
    primary = White,
    background = Black,        // HINTERGRUND = SCHWARZ
    surface = Black,
    onPrimary = Black,
    onBackground = White,      // TEXT AUF HINTERGRUND = WEISS
    onSurface = White
)

@Composable
fun MyLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}