package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = NeonPink,
  onPrimary = Color.White,
  primaryContainer = NeonPurple,
  onPrimaryContainer = Color.White,
  secondary = NeonBlue,
  onSecondary = Color.Black,
  tertiary = NeonAmber,
  background = ObsidianDark,
  onBackground = Color(0xFFF1F5F9),
  surface = SystemGrayDark,
  onSurface = Color(0xFFF1F5F9),
  surfaceVariant = GlassSurfaceDark,
  onSurfaceVariant = SystemTextSecondary,
  outline = GlassBorderLight
)

private val LightColorScheme = lightColorScheme(
  primary = NeonPink,
  onPrimary = Color.White,
  secondary = NeonPurple,
  background = Color(0xFF0F172A),
  surface = Color(0xFF1E293B),
  onBackground = Color.White,
  onSurface = Color.White
)

@Composable
fun AnkitVerseTheme(
  darkTheme: Boolean = true, // Default to sleek iOS Dark Glass aesthetic
  content: @Composable () -> Unit
) {
  val colors = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    content = content
  )
}
