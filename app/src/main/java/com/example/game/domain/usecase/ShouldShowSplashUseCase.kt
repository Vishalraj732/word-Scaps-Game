package com.example.game.domain.usecase

import com.example.game.common.Constant
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class ShouldShowSplashUseCase @Inject constructor() {
    suspend operator fun invoke(): Boolean {
        delay(Constant.SPLASH_TIME_OUT.milliseconds)
        return true
    }
}