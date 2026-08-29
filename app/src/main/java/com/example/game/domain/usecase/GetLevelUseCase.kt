package com.example.game.domain.usecase

import com.example.game.domain.model.game.Level
import com.example.game.domain.repository.game.LevelRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLevelUseCase @Inject constructor(
    private val repository: LevelRepository
) {
    private var cachedLevels: List<Level> = emptyList()

    suspend operator fun invoke(levelIndex: Int): Level {
        if (cachedLevels.isEmpty()) {
            cachedLevels = repository.loadLevelsFromJson()
        }

        val safeIndex = if (levelIndex in cachedLevels.indices) levelIndex else 0
        val level = cachedLevels[safeIndex]

        return level.copy(letters = level.letters.shuffled())
    }
}