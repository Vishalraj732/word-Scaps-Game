package com.example.game.presentation.screens.game

import androidx.compose.ui.graphics.Color
import com.example.game.R
import com.example.game.domain.model.game.Level
import com.example.game.domain.model.game.WordPosition

data class GameState(
    val currentLevelNum: Int = 1,
    val backgroundImageRes: Int = R.drawable.level_1,
    val levelData: Level? = null,
    val letters: List<Char> = emptyList(),
    val currentSwipeWord: String = "",
    val foundWords: List<String> = emptyList(),
    val isLevelComplete: Boolean = false,
    val themeColor: Color = Color(0xFF7E57C2),
    val flyingPuzzleWord: WordPosition? = null,
    val levelCompletedText: String = ""
)

sealed class GameIntent {
    object LoadLevel : GameIntent()
    data class UpdateSwipeWord(val word: String) : GameIntent()
    object SubmitWord : GameIntent()
    data class LoadSpecificLevel(val levelNum: Int) : GameIntent()
    data object ReloadCurrentLevel : GameIntent()
}

sealed class GameEffect {
    data class ShowMessage(val message: String) : GameEffect()
    object TriggerFireworks : GameEffect()
    object LevelCompletedAnimation : GameEffect()
}