package com.example.game.domain.usecase

import com.example.game.domain.model.game.Level
import javax.inject.Inject

class ValidateWordUseCase @Inject constructor() {

    sealed class Result {
        object Invalid : Result()
        object AlreadyFound : Result()
        data class Valid(val updatedFoundWords: List<String>, val isLevelComplete: Boolean) : Result()
    }

    operator fun invoke(word: String, currentLevel: Level, foundWords: List<String>): Result {
        val isValidWord = currentLevel.puzzleWords.any { it.word == word }
        val isAlreadyFound = foundWords.contains(word)

        return when {
            !isValidWord -> Result.Invalid
            isAlreadyFound -> Result.AlreadyFound
            else -> {
                val newFoundWords = foundWords + word
                val isLevelComplete = newFoundWords.size == currentLevel.puzzleWords.size
                Result.Valid(newFoundWords, isLevelComplete)
            }
        }
    }
}