package com.example.game.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.R
import com.example.game.common.GamePreferences
import com.example.game.domain.usecase.GetLevelUseCase
import com.example.game.domain.usecase.GetTotalLevelsUseCase
import com.example.game.domain.usecase.ValidateWordUseCase
import com.example.game.presentation.theme.getThemeColorForLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getLevelUseCase: GetLevelUseCase,
    private val getTotalLevelsUseCase: GetTotalLevelsUseCase,
    private val validateWordUseCase: ValidateWordUseCase,
    private val gamePrefs: GamePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<GameEffect>()
    val effect: SharedFlow<GameEffect> = _effect.asSharedFlow()

    private var currentLevelIndex = 0
    private var maxLevels = 0

    val levelCompletedWords = listOf("Awesome!", "Excellent!", "Fantastic!", "Great Job!", "Brilliant!", "Perfect!")

    private val levelBackgrounds = listOf(
        R.drawable.level_1, R.drawable.level_2, R.drawable.level_3, R.drawable.level_4, R.drawable.level_5
    )

    init {
        viewModelScope.launch {
            maxLevels = getTotalLevelsUseCase()
        }
    }

    fun processIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.LoadLevel -> loadLevel()
            is GameIntent.LoadSpecificLevel -> loadSpecificLevel(intent.levelNum)
            is GameIntent.UpdateSwipeWord -> updateWord(intent.word)
            is GameIntent.SubmitWord -> validateAndSubmitWord()
            is GameIntent.ReloadCurrentLevel -> reloadCurrentLevel()
        }
    }

    private fun loadLevel() {
        viewModelScope.launch {
            val (savedIndex, _, savedWords) = gamePrefs.gameProgressFlow.first()

            // 🌟 SAFETY CHECK: Agar saved index 20 ya usse bada hai (OutOfBounds), toh wapas 0 (Level 1) kar do
            currentLevelIndex = if (savedIndex >= maxLevels) 0 else savedIndex

            // USE CASE CALL
            val currentLevel = getLevelUseCase(currentLevelIndex)
            val freshThemeColor = getThemeColorForLevel(currentLevelIndex)
            val selectedBackground = levelBackgrounds.random()

            _state.update {
                it.copy(
                    currentLevelNum = currentLevel.levelNumber,
                    backgroundImageRes = selectedBackground,
                    levelData = currentLevel,
                    letters = currentLevel.letters,
                    foundWords = savedWords.toList(),
                    isLevelComplete = false,
                    themeColor = freshThemeColor
                )
            }
        }
    }

    private fun loadSpecificLevel(levelNum: Int) {
        viewModelScope.launch {
            // 🌟 User jo level click karega, wo load hoga
            currentLevelIndex = (levelNum - 1).coerceIn(0, maxLevels - 1) // Safety range lock

            // USE CASE CALL
            val currentLevel = getLevelUseCase(currentLevelIndex)
            val freshThemeColor = getThemeColorForLevel(currentLevelIndex)
            val selectedBackground = levelBackgrounds.random()

            _state.update {
                it.copy(
                    currentLevelNum = currentLevel.levelNumber,
                    backgroundImageRes = selectedBackground,
                    levelData = currentLevel,
                    letters = currentLevel.letters,
                    foundWords = emptyList(),
                    isLevelComplete = false,
                    themeColor = freshThemeColor
                )
            }

            gamePrefs.saveProgress(currentLevelIndex, emptySet())
        }
    }

    private fun reloadCurrentLevel() {
        viewModelScope.launch {
            val currentLevel = getLevelUseCase(currentLevelIndex)
            val freshThemeColor = getThemeColorForLevel(currentLevelIndex)
            val selectedBackground = levelBackgrounds.random()

            _state.update {
                it.copy(
                    currentLevelNum = currentLevel.levelNumber,
                    backgroundImageRes = selectedBackground,
                    levelData = currentLevel,
                    letters = currentLevel.letters,
                    foundWords = emptyList(),
                    isLevelComplete = false,
                    currentSwipeWord = "",
                    themeColor = freshThemeColor
                )
            }
            gamePrefs.saveProgress(currentLevelIndex, emptySet())
        }
    }

    private fun updateWord(word: String) {
        _state.update { it.copy(currentSwipeWord = word) }
    }

    private fun validateAndSubmitWord() {
        val currentWord = _state.value.currentSwipeWord
        val currentLevelData = _state.value.levelData ?: return
        val currentFoundWords = _state.value.foundWords

        // USE CASE CALL
        val validationResult = validateWordUseCase(currentWord, currentLevelData, currentFoundWords)

        when (validationResult) {
            is ValidateWordUseCase.Result.Valid -> {
                val matchingWord = currentLevelData.puzzleWords.find { it.word == currentWord }

                _state.update {
                    it.copy(
                        currentSwipeWord = currentWord,
                        flyingPuzzleWord = matchingWord
                    )
                }

                viewModelScope.launch {
                    _effect.emit(GameEffect.TriggerFireworks)
                    delay(600.milliseconds)

                    _state.update {
                        it.copy(
                            flyingPuzzleWord = null,
                            currentSwipeWord = "",
                            foundWords = validationResult.updatedFoundWords,
                            isLevelComplete = validationResult.isLevelComplete,
                            levelCompletedText = levelCompletedWords.random()
                        )
                    }

                    // 🌟 Level completion logic
                    if (validationResult.isLevelComplete) {

                        // 🌟 LOOP LOGIC: Agar Level 20 (index 19) pe hai, toh agla level 1 (index 0) hoga
                        val nextLevelIndex = if (currentLevelIndex >= maxLevels - 1) {
                            0
                        } else {
                            currentLevelIndex + 1
                        }
                        gamePrefs.saveProgress(nextLevelIndex, emptySet())

//                        _effect.emit(GameEffect.ShowMessage("Level Complete! 🎉"))
                        delay(1500.milliseconds)

                        // Current tracker ko bhi update kar do
                        currentLevelIndex = nextLevelIndex

                    } else {
                        // Level complete nahi hua hai, bas word save karo
                        gamePrefs.saveProgress(currentLevelIndex, validationResult.updatedFoundWords.toSet())
                    }
                }
            }
            is ValidateWordUseCase.Result.AlreadyFound -> {
                _state.update { it.copy(currentSwipeWord = "") }
                viewModelScope.launch { _effect.emit(GameEffect.ShowMessage("Already found!")) }
            }
            is ValidateWordUseCase.Result.Invalid -> {
                _state.update { it.copy(currentSwipeWord = "") }
                viewModelScope.launch { _effect.emit(GameEffect.ShowMessage("Invalid Word")) }
            }
        }
    }
}