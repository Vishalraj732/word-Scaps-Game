package com.example.game.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.game.R
import com.example.game.presentation.navigation.HomeRoute
import com.example.game.presentation.navigation.SplashRoute

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
){
    val shouldNavigate = viewModel.navigateToMain.collectAsStateWithLifecycle().value

    if (shouldNavigate){
        navController.navigate(HomeRoute){
            popUpTo(SplashRoute){
                inclusive = true
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)

    ){
        Image(
            painter = rememberAsyncImagePainter(R.drawable.splash_bg),
            contentDescription = "Splash Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}