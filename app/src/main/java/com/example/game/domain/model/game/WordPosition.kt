package com.example.game.domain.model.game

import kotlinx.serialization.Serializable

@Serializable
data class WordPosition(
    val word: String,
    val startX: Int,
    val startY: Int,
    val isHorizontal: Boolean
)