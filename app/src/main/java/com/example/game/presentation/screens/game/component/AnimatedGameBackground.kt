package com.example.game.presentation.screens.game.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedGameBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    val xOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x_offset"
    )

    val yOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y_offset"
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0F0C29), // Deep Dark Purple/Black
            Color(0xFF302B63), // Royal Purple
            Color(0xFF1E1E2C), // Your existing theme color
            Color(0xFF24243E)  // Dark Blueish
        ),
        start = Offset(xOffset, yOffset),
        end = Offset(xOffset + 800f, yOffset + 1000f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {
        content()
    }
}