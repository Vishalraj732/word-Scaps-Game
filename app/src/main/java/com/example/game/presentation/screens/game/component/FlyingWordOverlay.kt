package com.example.game.presentation.screens.game.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.domain.model.game.WordPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun FlyingWordOverlay(
    puzzleWord: WordPosition,
    themeColor: Color,
    startOffset: Offset,
    gridCellPositions: Map<Pair<Int, Int>, Offset>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        puzzleWord.word.forEachIndexed { index, char ->
            val gridX = if (puzzleWord.isHorizontal) puzzleWord.startX + index else puzzleWord.startX
            val gridY = if (puzzleWord.isHorizontal) puzzleWord.startY else puzzleWord.startY + index

            val targetOffset = gridCellPositions[Pair(gridX, gridY)] ?: Offset.Zero

            val charStartX = startOffset.x + (index * 30f)

            val animX = remember { Animatable(charStartX) }
            val animY = remember { Animatable(startOffset.y) }
            val alphaAnim = remember { Animatable(1f) }

            LaunchedEffect(Unit) {
                delay((index * 50L).milliseconds)
                launch { animX.animateTo(targetOffset.x, tween(400, easing = FastOutSlowInEasing)) }
                launch { animY.animateTo(targetOffset.y, tween(400, easing = FastOutSlowInEasing)) }
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(animX.value.roundToInt(), animY.value.roundToInt()) }
                    .alpha(alphaAnim.value)
                    .size(34.dp)
                    .background(themeColor, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = char.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}