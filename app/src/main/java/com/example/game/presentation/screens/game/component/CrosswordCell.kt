package com.example.game.presentation.screens.game.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Individual Crossword Cell with Smooth Spring Pop-up Animation
 */
@Composable
fun CrosswordCell(
    letter: Char,
    isRevealed: Boolean,
    themeColor: Color,
    onPositioned: (Offset) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "cellPopAnimation"
    )

    Box(
        modifier = Modifier
            .size(34.dp)
            .scale(scale)
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInRoot())
            }
            .background(
                color = if (isRevealed) themeColor else Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = if (isRevealed) 1.5.dp else 1.dp,
                color = if (isRevealed) themeColor else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isRevealed) {
            Text(text = letter.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}