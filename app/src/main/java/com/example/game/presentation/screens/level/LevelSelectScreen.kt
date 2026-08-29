package com.example.game.presentation.screens.level

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.game.presentation.components.ToolbarComponent
import com.example.game.presentation.navigation.GameRoute
import com.example.game.presentation.navigation.HomeRoute
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(
    navController: NavController,
    viewModel: LevelSelectViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is LevelSelectEvent.NavigateToGame -> {
                    navController.navigate(GameRoute(levelId = event.levelNum)) {
                        popUpTo(HomeRoute) { inclusive = false }
                    }
                }
                is LevelSelectEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF1E1E2C),
        topBar = {
            ToolbarComponent(
                showBackButton = true
            ) { navController.popBackStack() }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Text(
                "Select Level",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(state.levels) { levelItem ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable(enabled = !levelItem.isLocked) {
                                    viewModel.onAction(LevelSelectAction.OnLevelClicked(levelItem.levelNum))
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (levelItem.isLocked) Color(0xFF2D2D44) else Color(0xFF4FACFE)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (levelItem.isLocked) 0.dp else 6.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                if (levelItem.isLocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${levelItem.levelNum}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}