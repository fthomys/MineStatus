package me.fthomys.minestatus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val MinecraftGreen = Color(0xFF5E7330)
val MinecraftDarkGreen = Color(0xFF3B511F)
val MinecraftBrown = Color(0xFF825432)
val MinecraftDarkBrown = Color(0xFF5C3C24)
val MinecraftGray = Color(0xFF828282)
val MinecraftDarkGray = Color(0xFF383838)
val MinecraftBlack = Color(0xFF000000)
val MinecraftWhite = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = MinecraftGreen,
    onPrimary = MinecraftWhite,
    primaryContainer = MinecraftDarkGreen,
    onPrimaryContainer = MinecraftWhite,
    secondary = MinecraftBrown,
    onSecondary = MinecraftWhite,
    secondaryContainer = MinecraftDarkBrown,
    onSecondaryContainer = MinecraftWhite,
    background = Color(0xFFF5F5F5),
    onBackground = MinecraftBlack,
    surface = MinecraftWhite,
    onSurface = MinecraftBlack
)

private val DarkColorScheme = darkColorScheme(
    primary = MinecraftGreen,
    onPrimary = MinecraftWhite,
    primaryContainer = MinecraftDarkGreen,
    onPrimaryContainer = MinecraftWhite,
    secondary = MinecraftBrown,
    onSecondary = MinecraftWhite,
    secondaryContainer = MinecraftDarkBrown,
    onSecondaryContainer = MinecraftWhite,
    background = MinecraftDarkGray,
    onBackground = MinecraftWhite,
    surface = MinecraftGray,
    onSurface = MinecraftWhite
)

@Composable
fun MineStatusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}