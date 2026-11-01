package com.howverse.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Every colour the app draws comes from here, so the dark and light themes
 * always stay in step with each other.
 */
data class Palette(
    val isDark: Boolean,
    val gradient: List<Color>,
    val ink: Color,
    val text: Color,
    val softText: Color,
    val panel: Color,
    val glass: Color,
    val glassBorder: Color,
    val cardBorder: Color,
    val accent: Color,
    val onAccent: Color,
    val heroBg: Color,
    val heroBorder: Color,
    val navBg: Color,
    val indicator: Color,
    val bubbleAi: Color,
    val bubbleUser: Color,
    val onBubbleUser: Color,
    val divider: Color,
    val danger: Color,
    val good: Color
)

val DarkPalette = Palette(
    isDark = true,
    gradient = listOf(Color(0xFF111A2B), Color(0xFF080B13), Color(0xFF171326)),
    ink = Color(0xFF080B13),
    text = Color(0xFFF2F5FB),
    softText = Color(0xFFB8C2D6),
    panel = Color(0xFF171D2B),
    glass = Color(0xCC252E40),
    glassBorder = Color(0xFF46516A),
    cardBorder = Color(0x334F6388),
    accent = Color(0xFF9DBBFF),
    onAccent = Color(0xFF0B1020),
    heroBg = Color(0xAA283957),
    heroBorder = Color(0x334F6388),
    navBg = Color(0xEE101725),
    indicator = Color(0xFF293954),
    bubbleAi = Color(0xFF1D2637),
    bubbleUser = Color(0xFF9DBBFF),
    onBubbleUser = Color(0xFF0B1020),
    divider = Color(0x1FFFFFFF),
    danger = Color(0xFFFFB4AB),
    good = Color(0xFF8CE0B0)
)

val LightPalette = Palette(
    isDark = false,
    gradient = listOf(Color(0xFFF7FAFF), Color(0xFFFFFFFF), Color(0xFFEDF2FF)),
    ink = Color(0xFFF6F9FE),
    text = Color(0xFF0C1524),
    softText = Color(0xFF5A6884),
    panel = Color(0xFFFFFFFF),
    glass = Color(0xF2FFFFFF),
    glassBorder = Color(0xFFCBD8EE),
    cardBorder = Color(0x66A9BEDF),
    accent = Color(0xFF2F5BD0),
    onAccent = Color(0xFFFFFFFF),
    heroBg = Color(0xFFE7EEFF),
    heroBorder = Color(0x66A9BEDF),
    navBg = Color(0xF7FFFFFF),
    indicator = Color(0xFFDCE6FF),
    bubbleAi = Color(0xFFFFFFFF),
    bubbleUser = Color(0xFF2F5BD0),
    onBubbleUser = Color(0xFFFFFFFF),
    divider = Color(0x14000000),
    danger = Color(0xFFB3261E),
    good = Color(0xFF1B7F4B)
)

val LocalPalette = staticCompositionLocalOf { DarkPalette }

@Composable
fun HowverseTheme(isDark: Boolean, content: @Composable () -> Unit) {
    val p = if (isDark) DarkPalette else LightPalette
    val scheme = if (isDark) {
        darkColorScheme(
            background = p.ink, surface = p.panel, primary = p.accent, onPrimary = p.onAccent,
            onBackground = p.text, onSurface = p.text, surfaceVariant = p.panel,
            onSurfaceVariant = p.softText
        )
    } else {
        lightColorScheme(
            background = p.ink, surface = p.panel, primary = p.accent, onPrimary = p.onAccent,
            onBackground = p.text, onSurface = p.text, surfaceVariant = p.panel,
            onSurfaceVariant = p.softText
        )
    }
    CompositionLocalProvider(LocalPalette provides p) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
