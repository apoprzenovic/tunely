package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rit.tunely.data.state.GameUiState
import com.rit.tunely.ui.screens.game.GameViewModel

@Composable
fun PlayScreen(state: GameUiState, vm: GameViewModel) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.isLoading -> Text("Loading track…")
            state.error != null -> Text(state.error, color = MaterialTheme.colorScheme.error)
            state.track != null -> GuessGrid(state, Modifier.padding(bottom = 16.dp))
            else -> Text("Could not load track.")
        }
    }

    if (state.track != null && !state.gameFinished && state.error == null) {
        OnScreenKeyboard(
            modifier = Modifier.padding(bottom = 8.dp),
            onCharClick = vm::appendToGuess,
            onEnterClick = vm::submitGuess,
            onBackspaceClick = vm::deleteLastChar
        )
    } else {
        Spacer(Modifier.height(50.dp))
    }
}