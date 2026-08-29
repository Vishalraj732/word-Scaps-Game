package com.example.game.presentation.screens.level

data class LevelSelectState(
    val levels: List<LevelItem> = emptyList(),
    val isLoading: Boolean = false
)

data class LevelItem(
    val levelNum: Int,
    val isLocked: Boolean
)

sealed interface LevelSelectAction {
    data class OnLevelClicked(val levelNum: Int) : LevelSelectAction
    data object OnBackPressed : LevelSelectAction
}

sealed interface LevelSelectEvent {
    data class NavigateToGame(val levelNum: Int) : LevelSelectEvent
    data object NavigateBack : LevelSelectEvent
}