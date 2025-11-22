package com.kittel.ultraminimallauncher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,        // Farbe für wichtige Buttons
    background = White,        // Haupt-Hintergrundfarbe
    surface = LightBlue,       // Hintergrundfarbe für Karten/Listenelemente
    onPrimary = White,         // Textfarbe auf primären Buttons
    onBackground = DarkBlue,   // Haupt-Textfarbe
    onSurface = DarkBlue       // Textfarbe auf Karten
)

val textShadow = Shadow(
    color = Color.Black.copy(alpha = 0.6f),
    blurRadius = 4f,
    offset = Offset(8f, 8f),
)


// DAS IST DIE ENTSCHEIDENDE FARBPALETTE
private val DarkColorScheme = darkColorScheme(
    primary = White,
    background = Black,        // HINTERGRUND = SCHWARZ
    surface = Black,
    onPrimary = Black,
    onBackground = White,      // TEXT AUF HINTERGRUND = WEISS
    onSurface = White,
)

@Composable
fun MyLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}