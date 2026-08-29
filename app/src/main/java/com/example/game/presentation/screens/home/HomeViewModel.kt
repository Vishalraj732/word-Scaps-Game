package com.example.game.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.common.GamePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gamePrefs: GamePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>()
    val event: SharedFlow<HomeEvent> = _event.asSharedFlow()

    init {
        observeGameProgress()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnPlayOrResumeClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToGame(action.levelNum))
                }
            }
            is HomeAction.OnAllLevelsClicked -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToLevelSelect)
                }
            }
        }
    }

    private fun observeGameProgress() {
        viewModelScope.launch {
            gamePrefs.gameProgressFlow.collect { progress ->
                val currentLevelNum = progress.first + 1
                val isResume = currentLevelNum > 1

                _state.update {
                    it.copy(
                        savedLevelNumber = currentLevelNum,
                        isResumeAvailable = isResume,
                        isLoading = false
                    )
                }
            }
        }
    }
}