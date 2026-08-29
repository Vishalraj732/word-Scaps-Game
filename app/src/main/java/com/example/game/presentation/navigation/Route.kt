package com.example.game.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object HomeRoute

@Serializable
object LevelSelectRoute

@Serializable
data class GameRoute(val levelId: Int = 1)