package com.rit.tunely.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.ui.screens.game.components.GameEndDialog
import com.rit.tunely.ui.screens.game.components.GuessGrid
import com.rit.tunely.ui.screens.game.components.HintDialog
import com.rit.tunely.ui.screens.game.components.OnScreenKeyboard

val PastelGreen = Color(0xFFAEFFD0)
val PastelYellow = Color(0xFFFFFC9C)
val PastelGray = Color(0xFFD3D3D3)
val BackgroundGray = Color(0xFFBDBDBD)
val BorderGray = Color(0xFFBDBDBD)
val PastelRed = Color(0xFFFFD1D1)
val Purple200 = Color(0xFFBB86FC)

@Composable
fun GameScreen(
    nav: NavController,
    vm: GameViewModel = hiltViewModel()
) {
    var showGameContent by remember { mutableStateOf(false) }
    val state by vm.state.collectAsState()
    var showHintDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { showHintDialog = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                    contentDescription = "Hint",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 0.dp, 25.dp, 0.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TUNELY",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.size(24.dp))
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (!showGameContent) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            showGameContent = true
                            vm.loadTrack()
                        },
                        modifier = Modifier.size(120.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PlayCircle,
                            contentDescription = "Play Game",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    if (state.isLoading) {
                        Text("Loading track...")
                    } else if (state.error != null && !state.gameFinished) {
                        Text(state.error!!, color = MaterialTheme.colorScheme.error)
                    } else if (state.track != null) {
                        GuessGrid(
                            state = state,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    } else if (!state.isLoading && !state.gameFinished) {
                        Text("Could not load track details.")
                    }
                }

                if (state.track != null && !state.gameFinished && state.error == null) {
                    OnScreenKeyboard(
                        onCharClick = { char -> vm.appendToGuess(char) },
                        onEnterClick = { vm.submitGuess() },
                        onBackspaceClick = { vm.deleteLastChar() },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                } else if (showGameContent) {
                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }

    if (showHintDialog) {
        HintDialog(onDismiss = { showHintDialog = false })
    }

    if (state.gameFinished) {
        GameEndDialog(
            gameWon = state.gameWon,
            pointsEarned = state.pointsEarnedThisRound,
            onContinue = {
                vm.resetGame()
                showGameContent = false
            }
        )
    }
}