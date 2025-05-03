package com.rit.tunely.data.state

import com.rit.tunely.data.model.Track
import com.rit.tunely.util.Constants

data class GameUiState(
    val track: Track? = null,
    val maskedTitle: String = "",
    val attemptsLeft: Int = Constants.GUESSES_TOTAL,
    val currentGuess: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)