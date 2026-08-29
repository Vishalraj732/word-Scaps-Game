package com.example.game.domain.model.game

import kotlinx.serialization.Serializable

@Serializable
data class Level(
    val levelNumber: Int,
    val letters: List<Char>,
    val puzzleWords: List<WordPosition>
)