package com.example.game.presentation.screens.game.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.iterator
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LetterWheel(
    letters: List<Char>,
    themeColor: Color,
    isSwipeEnabled: Boolean = true,
    onWordSwiped: (String) -> Unit,
    onSwipeFinished: () -> Unit
) {
    var wheelSize by remember { mutableStateOf(IntSize.Zero) }

    var selectedIndices by remember(letters) { mutableStateOf(emptyList<Int>()) }
    var currentDragPosition by remember(letters) { mutableStateOf<Offset?>(null) }

    val letterPositions = remember(wheelSize, letters) {
        val positions = mutableMapOf<Int, Offset>()
        if (wheelSize.width > 0 && letters.isNotEmpty()) {
            val center = Offset(wheelSize.width / 2f, wheelSize.height / 2f)
            val radius = (wheelSize.width / 2f) * 0.65f
            val angleStep = (2 * Math.PI) / letters.size

            letters.indices.forEach { i ->
                val angle = i * angleStep - (Math.PI / 2)
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()
                positions[i] = Offset(x, y)
            }
        }
        positions
    }

    Box(
        modifier = Modifier
            .size(280.dp)
            .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
            .onGloballyPositioned { coordinates ->
                wheelSize = coordinates.size
            }
            .pointerInput(letters, isSwipeEnabled) {
                if (isSwipeEnabled) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val closestIndex = getClosestLetterIndex(offset, letterPositions, 100f)
                            if (closestIndex != null && closestIndex < letters.size) { // Safety check added
                                selectedIndices = listOf(closestIndex)
                                onWordSwiped(letters[closestIndex].toString())
                            }
                            currentDragPosition = offset
                        },
                        onDrag = { change, _ ->
                            currentDragPosition = change.position
                            val closestIndex = getClosestLetterIndex(change.position, letterPositions, 100f)

                            if (closestIndex != null && closestIndex < letters.size && !selectedIndices.contains(closestIndex)) {
                                selectedIndices = selectedIndices + closestIndex

                                val currentWord = selectedIndices
                                    .mapNotNull { letters.getOrNull(it) }
                                    .joinToString("")

                                onWordSwiped(currentWord)
                            }
                        },
                        onDragEnd = {
                            selectedIndices = emptyList()
                            currentDragPosition = null
                            onSwipeFinished()
                        }
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (selectedIndices.size >= 2) {
                for (i in 0 until selectedIndices.size - 1) {
                    val start = letterPositions[selectedIndices[i]] ?: continue
                    val end = letterPositions[selectedIndices[i + 1]] ?: continue
                    drawLine(
                        color = themeColor,
                        start = start,
                        end = end,
                        strokeWidth = 24f,
                        cap = StrokeCap.Round
                    )
                }
            }

            if (selectedIndices.isNotEmpty() && currentDragPosition != null) {
                val start = letterPositions[selectedIndices.last()]
                if (start != null) {
                    drawLine(
                        color = themeColor.copy(alpha = 0.6f),
                        start = start,
                        end = currentDragPosition!!,
                        strokeWidth = 24f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        letterPositions.forEach { (index, offset) ->
            val isSelected = selectedIndices.contains(index)

            Box(
                modifier = Modifier
                    .offset {
                        val centerX = wheelSize.width / 2f
                        val centerY = wheelSize.height / 2f
                        IntOffset(
                            (offset.x - centerX).toInt(),
                            (offset.y - centerY).toInt()
                        )
                    }
                    .size(50.dp)
                    .background(
                        color = if (isSelected) themeColor else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                val letterText = letters.getOrNull(index)?.toString() ?: ""

                Text(
                    text = letterText,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getClosestLetterIndex(
    touchPoint: Offset,
    letterPositions: Map<Int, Offset>,
    threshold: Float
): Int? {
    var minDistance = Float.MAX_VALUE
    var closestIndex: Int? = null

    for ((index, pos) in letterPositions) {
        val distance = (touchPoint - pos).getDistance()
        if (distance < minDistance && distance <= threshold) {
            minDistance = distance
            closestIndex = index
        }
    }
    return closestIndex
}