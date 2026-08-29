package com.example.game.domain.repository.game

import com.example.game.domain.model.game.Level

interface LevelRepository{
    suspend fun loadLevelsFromJson(): List<Level>
}