package com.routelligence.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1E88E5),
    secondary = androidx.compose.ui.graphics.Color(0xFF43A047),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFB8C00),
    background = androidx.compose.ui.graphics.Color(0xFFF6F8FC),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF90CAF9),
    secondary = androidx.compose.ui.graphics.Color(0xFFA5D6A7),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFCC80),
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    surface = androidx.compose.ui.graphics.Color(0xFF1E1E1E)
)

@Composable
fun RoutelligenceTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
