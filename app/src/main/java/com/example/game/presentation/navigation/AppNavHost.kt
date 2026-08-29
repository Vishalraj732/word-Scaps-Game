package com.example.game.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.game.presentation.screens.game.GameScreen
import com.example.game.presentation.screens.home.HomeScreen
import com.example.game.presentation.screens.level.LevelSelectScreen
import com.example.game.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashRoute){

        composable<SplashRoute> {
            SplashScreen(navController)
        }

        // 1. Home Screen
        composable<HomeRoute> {
            HomeScreen(
                navController =  navController
            )
        }

        // 2. Level Select Screen
        composable<LevelSelectRoute> {
            LevelSelectScreen(
                navController = navController
            )
        }

        // 3. Game Screen
        composable<GameRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<GameRoute>()

            GameScreen(
                levelId = route.levelId,
                navController = navController
            )
        }
    }
}