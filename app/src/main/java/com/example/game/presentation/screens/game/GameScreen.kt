package com.example.game.presentation.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.game.presentation.components.ToolbarComponent
import com.example.game.presentation.screens.game.component.CrosswordGridView
import com.example.game.presentation.screens.game.component.FlyingWordOverlay
import com.example.game.presentation.screens.game.component.LetterWheel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    levelId: Int = -1,
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // 🌟 Snackbar
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 🌟 GLOBAL COORDINATES TRACKER
    val gridCellPositions = remember { mutableStateMapOf<Pair<Int, Int>, Offset>() }
    var startPosition by remember { mutableStateOf(Offset.Zero) }
    var showWordFireworks by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GameEffect.ShowMessage -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(effect.message)
                    }
                }
                is GameEffect.TriggerFireworks -> {
                    showWordFireworks = true
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(levelId) {
        if (levelId != -1) viewModel.processIntent(GameIntent.LoadSpecificLevel(levelId))
        else viewModel.processIntent(GameIntent.LoadLevel)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ToolbarComponent(
                title = "Level ${state.currentLevelNum}",
                showBackButton = true
            ) { navController.popBackStack() }
        }
    ) { paddingValues ->
        // Main Screen Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1E1E2C)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(state.backgroundImageRes),
                contentDescription = "Nature Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Crossword Grid
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    state.levelData?.let { level ->
                        CrosswordGridView(
                            puzzleWords = level.puzzleWords,
                            foundWords = state.foundWords,
                            themeColor = state.themeColor,
                            onCellPositioned = { x, y, offset ->
                                gridCellPositions[Pair(x, y)] = offset // Save exact cell coordinate
                            }
                        )
                    }

                    if (showWordFireworks) {
                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.Asset("fireworks.json")
                        )
                        val progress by animateLottieCompositionAsState(
                            composition = composition,
                            iterations = 1
                        )

                        LaunchedEffect(progress) {
                            if (progress == 1f) {
                                showWordFireworks = false
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.size(350.dp)
                            )
                        }
                    }
                }

                if (state.currentSwipeWord.isNotEmpty()) {
                    Text(
                        text = state.currentSwipeWord,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clip(RoundedCornerShape(50))
                            .background(state.themeColor)
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .onGloballyPositioned { coordinates ->
                                startPosition = coordinates.positionInRoot()
                            }
                    )
                } else {
                    Spacer(modifier = Modifier.height(56.dp))
                }

                // 3. Letter Wheel
                state.levelData?.let { _ ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LetterWheel(
                            letters = state.letters,
                            themeColor = state.themeColor,
                            isSwipeEnabled = !state.isLevelComplete,
                            onWordSwiped = { viewModel.processIntent(GameIntent.UpdateSwipeWord(it)) },
                            onSwipeFinished = { viewModel.processIntent(GameIntent.SubmitWord) }
                        )

                        IconButton(
                            onClick = { viewModel.processIntent(GameIntent.ReloadCurrentLevel) },
                            enabled = state.foundWords.isNotEmpty(),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 16.dp)
                                .background(
                                    color = if (state.foundWords.isNotEmpty()) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reload Level",
                                tint = if (state.foundWords.isNotEmpty()) Color.White else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // 🌟 LEVEL COMPLETE OVERLAY
            if (state.isLevelComplete) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                ) {

                    // 1. Center Text (Awesome & Level Completed)
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.levelCompletedText,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "Level Complete! 🎉",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(state.themeColor)
                                .padding(horizontal = 32.dp, vertical = 12.dp)
                        )
                    }

                    // 2. Bottom Button (Next Level)
                    Button(
                        onClick = {
                            viewModel.processIntent(GameIntent.LoadLevel)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = state.themeColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 48.dp)
                    ) {
                        Text(
                            text = "Next Level",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (state.flyingPuzzleWord != null) {
            FlyingWordOverlay(
                puzzleWord = state.flyingPuzzleWord!!,
                themeColor = state.themeColor,
                startOffset = startPosition,
                gridCellPositions = gridCellPositions
            )
        }
    }
}