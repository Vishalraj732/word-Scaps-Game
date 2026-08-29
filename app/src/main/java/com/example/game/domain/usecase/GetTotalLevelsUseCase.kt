package com.example.game.domain.usecase

import com.example.game.domain.repository.game.LevelRepository
import javax.inject.Inject

class GetTotalLevelsUseCase @Inject constructor(
    private val repository: LevelRepository
) {
    suspend operator fun invoke(): Int {
        return repository.loadLevelsFromJson().size
    }
}