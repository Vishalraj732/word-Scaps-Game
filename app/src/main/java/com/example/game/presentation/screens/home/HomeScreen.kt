package com.example.game.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.game.presentation.navigation.GameRoute
import com.example.game.presentation.navigation.LevelSelectRoute
import com.example.game.presentation.screens.game.component.AnimatedGameBackground
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is HomeEvent.NavigateToGame -> {
                    navController.navigate(GameRoute(levelId = event.levelNum))
                }
                is HomeEvent.NavigateToLevelSelect -> {
                    navController.navigate(LevelSelectRoute)
                }
            }
        }
    }

    AnimatedGameBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // TOP SECTION: GAME TITLE
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "WORD",
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )
                Text(
                    text = "PUZZLE",
                    color = Color(0xFFFACC15),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Train your brain daily!",
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }

            // BOTTOM SECTION: ACTION BUTTONS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    if (state.isResumeAvailable) {
                        // RESUME BUTTON
                        PremiumButton(
                            text = "RESUME LEVEL ${state.savedLevelNumber}",
                            icon = Icons.Default.PlayArrow,
                            gradientColors = listOf(Color(0xFF4FACFE), Color(0xFF3F51B5)),
                            onClick = {
                                viewModel.onAction(HomeAction.OnPlayOrResumeClicked(state.savedLevelNumber))
                            }
                        )

                        // LEVELS BUTTON
                        OutlinedPremiumButton(
                            text = "ALL LEVELS",
                            onClick = {
                                viewModel.onAction(HomeAction.OnAllLevelsClicked)
                            }
                        )
                    } else {
                        // PLAY BUTTON (Fresh Start)
                        PremiumButton(
                            text = "PLAY NOW",
                            icon = Icons.Default.PlayArrow,
                            gradientColors = listOf(Color(0xFF00b09b), Color(0xFF96c93d)),
                            onClick = {
                                viewModel.onAction(HomeAction.OnPlayOrResumeClicked(1))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Brush.horizontalGradient(gradientColors))
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp).padding(end = 8.dp)
                )
            }
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OutlinedPremiumButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}