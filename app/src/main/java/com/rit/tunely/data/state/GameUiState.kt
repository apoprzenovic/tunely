package com.rit.tunely.data.state

import com.rit.tunely.data.model.Track
import kotlinx.coroutines.flow.StateFlow

data class GameUiState(
    val track: Track? = null,
    val guesses: List<String> = emptyList(),
    val currentAttemptIndex: Int = 0,
    val currentGuess: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val gameFinished: Boolean = false,
    val gameWon: Boolean = false,
    val pointsEarnedThisRound: Int? = null,
    val isPlaying: StateFlow<Boolean>
)