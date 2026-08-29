package com.example.game.presentation.screens.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.game.domain.model.game.WordPosition

@Composable
fun CrosswordGridView(
    puzzleWords: List<WordPosition>,
    foundWords: List<String>,
    themeColor: Color,
    onCellPositioned: (Int, Int, Offset) -> Unit
) {
    val maxRow = puzzleWords.maxOfOrNull { if (it.isHorizontal) it.startY else it.startY + it.word.length - 1 } ?: 0
    val maxCol = puzzleWords.maxOfOrNull { if (it.isHorizontal) it.startX + it.word.length - 1 else it.startX } ?: 0

    val letterMap = mutableMapOf<Pair<Int, Int>, Char>()
    val revealMap = mutableMapOf<Pair<Int, Int>, Boolean>()

    puzzleWords.forEach { puzzleWord ->
        val isFound = foundWords.contains(puzzleWord.word)
        puzzleWord.word.forEachIndexed { index, char ->
            val x = if (puzzleWord.isHorizontal) puzzleWord.startX + index else puzzleWord.startX
            val y = if (puzzleWord.isHorizontal) puzzleWord.startY else puzzleWord.startY + index
            letterMap[Pair(x, y)] = char
            if (isFound) revealMap[Pair(x, y)] = true
        }
    }

    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            for (y in 0..maxRow) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (x in 0..maxCol) {
                        val char = letterMap[Pair(x, y)]
                        if (char != null) {
                            CrosswordCell(
                                letter = char,
                                isRevealed = revealMap[Pair(x, y)] == true,
                                onPositioned = { offset -> onCellPositioned(x, y, offset) },
                                themeColor = themeColor
                            )
                        } else {
                            Spacer(modifier = Modifier.size(34.dp))
                        }
                    }
                }
            }
        }
    }
}