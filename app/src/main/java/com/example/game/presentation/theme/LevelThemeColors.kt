package com.example.game.presentation.theme

import androidx.compose.ui.graphics.Color

val LevelThemeColors = listOf(
    Color(0xFF7E57C2), // Deep Purple (Level 1, 6, 11...)
    Color(0xFF42A5F5), // Ocean Blue (Level 2, 7, 12...)
    Color(0xFF66BB6A), // Forest Green (Level 3, 8, 13...)
    Color(0xFFEF5350), // Coral Red (Level 4, 9, 14...)
    Color(0xFFFFCA28)  // Amber Gold (Level 5, 10, 15...)
)

fun getThemeColorForLevel(levelIndex: Int): Color {
    return LevelThemeColors[levelIndex % LevelThemeColors.size]
}