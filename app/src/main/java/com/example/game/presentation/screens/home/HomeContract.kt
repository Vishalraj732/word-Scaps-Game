package com.example.game.presentation.screens.home


// 🌟 UI State
data class HomeState(
    val savedLevelNumber: Int = 1,
    val isResumeAvailable: Boolean = false,
    val isLoading: Boolean = true
)

// 🌟 UI Actions / Intents
sealed interface HomeAction {
    data class OnPlayOrResumeClicked(val levelNum: Int) : HomeAction
    data object OnAllLevelsClicked : HomeAction
}

// 🌟 One-time Side Effects / Events
sealed interface HomeEvent {
    data class NavigateToGame(val levelNum: Int) : HomeEvent
    data object NavigateToLevelSelect : HomeEvent
}