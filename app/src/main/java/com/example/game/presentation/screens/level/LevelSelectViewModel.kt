package com.example.game.presentation.screens.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.common.GamePreferences
import com.example.game.domain.usecase.GetTotalLevelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelSelectViewModel @Inject constructor(
    private val getTotalLevelsUseCase: GetTotalLevelsUseCase,
    private val gamePrefs: GamePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectState(isLoading = true))
    val state: StateFlow<LevelSelectState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<LevelSelectEvent>()
    val event: SharedFlow<LevelSelectEvent> = _event.asSharedFlow()

    init {
        loadLevels()
    }

    fun onAction(action: LevelSelectAction) {
        when (action) {
            is LevelSelectAction.OnLevelClicked -> {
                viewModelScope.launch {
                    _event.emit(LevelSelectEvent.NavigateToGame(action.levelNum))
                }
            }
            is LevelSelectAction.OnBackPressed -> {
                viewModelScope.launch {
                    _event.emit(LevelSelectEvent.NavigateBack)
                }
            }
        }
    }

    private fun loadLevels() {
        viewModelScope.launch {
            gamePrefs.gameProgressFlow.collect { progress ->
                val unlockedLevelNum = progress.second + 1
                val totalLevels = getTotalLevelsUseCase()

                val levelItems = (1..totalLevels).map { levelNum ->
                    LevelItem(
                        levelNum = levelNum,
                        isLocked = levelNum > unlockedLevelNum
                    )
                }

                _state.update {
                    it.copy(
                        levels = levelItems,
                        isLoading = false
                    )
                }
            }
        }
    }
}