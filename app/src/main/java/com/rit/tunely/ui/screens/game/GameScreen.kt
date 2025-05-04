package com.rit.tunely.ui.screens.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.ui.screens.game.components.GameEndDialog
import com.rit.tunely.ui.screens.game.components.GameTopBar
import com.rit.tunely.ui.screens.game.components.HintDialog
import com.rit.tunely.ui.screens.game.components.PlayScreen
import com.rit.tunely.ui.screens.game.components.StartScreen

@Composable
fun GameScreen(
    nav: NavController,
    vm: GameViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val isPlayingState by state.isPlaying.collectAsState()
    var hintVisible by remember { mutableStateOf(false) }
    var playing by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GameTopBar(
            onHint = { hintVisible = true },
            isPlaying = isPlayingState,
            showPlaybackControls = playing && !state.gameFinished && state.track != null,
            onPlayPauseClick = vm::togglePlayPause
        )

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = .5f))

        when {
            !playing -> StartScreen { playing = true; vm.loadTrack() }
            else -> PlayScreen(state = state, vm = vm)
        }
    }

    if (hintVisible) HintDialog { hintVisible = false }

    if (state.gameFinished) {
        GameEndDialog(
            gameWon = state.gameWon,
            pointsEarned = state.pointsEarnedThisRound,
            trackTitle = state.track?.title ?: "Unknown Title",
            onContinue = { vm.resetGame(); playing = false }
        )
    }
}