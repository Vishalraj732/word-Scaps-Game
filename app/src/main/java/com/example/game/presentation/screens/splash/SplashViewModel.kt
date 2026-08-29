package com.example.game.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.game.domain.usecase.ShouldShowSplashUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val shouldSplashUseCase: ShouldShowSplashUseCase
): ViewModel(){
    var navigateToMain = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            if (shouldSplashUseCase()){
                navigateToMain.value = true
            }
        }
    }
}