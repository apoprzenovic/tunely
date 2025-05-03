package com.rit.tunely.ui.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.repository.ITunesRepository
import com.rit.tunely.data.state.GameUiState
import com.rit.tunely.player.AudioPlayer
import com.rit.tunely.util.Constants
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: ITunesRepository,
    private val player: AudioPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state

    // This will be updated by the custom keyboard later
    fun appendToGuess(char: Char) {
        val currentTrackTitle = _state.value.track?.title ?: return
        if (_state.value.currentGuess.length < currentTrackTitle.length) {
            _state.update { it.copy(currentGuess = it.currentGuess + char) }
        }
    }

    // This will be updated by the custom keyboard later
    fun deleteLastChar() {
        if (_state.value.currentGuess.isNotEmpty()) {
            _state.update { it.copy(currentGuess = it.currentGuess.dropLast(1)) }
        }
    }

    // This will be triggered by an 'Enter' key on the custom keyboard later
    fun submitGuess() {
        val currentState = _state.value
        val track = currentState.track ?: return
        val guess = currentState.currentGuess.uppercase() // Ensure comparison is case-insensitive

        // Basic validation: Ensure guess has the correct length
        if (guess.length != track.title.length) {
            Log.w("GameViewModel", "Attempted to submit guess with incorrect length.")
            // Optionally provide feedback to the user here
            return
        }

        // Add the current guess to the list of submitted guesses
        val updatedGuesses = currentState.guesses + guess
        val nextAttemptIndex = currentState.currentAttemptIndex + 1

        val gameWon = guess.equals(track.title, ignoreCase = true)
        val attemptsExhausted = nextAttemptIndex >= Constants.GUESSES_TOTAL
        val gameFinished = gameWon || attemptsExhausted

        if (gameFinished) {
            player.stop() // Stop playback on game end
        }

        _state.update {
            it.copy(
                guesses = updatedGuesses,
                currentAttemptIndex = nextAttemptIndex,
                currentGuess = "", // Clear the input field for the next guess/attempt
                gameFinished = gameFinished,
                gameWon = gameWon
                // error = if (!gameWon && attemptsExhausted) "You lost!" else null // Error state for loss? Or use gameFinished/gameWon
            )
        }
        Log.d("GameViewModel", "Guess Submitted: $guess, Attempt: $nextAttemptIndex, Won: $gameWon, Finished: $gameFinished")
        Log.d("GameViewModel", "Updated Guesses: $updatedGuesses")
    }


    fun loadTrack() = viewModelScope.launch {
        _state.update {
            GameUiState(isLoading = true) // Reset state completely on new track load
        }
        player.stop() // Stop any previous playback

        when (val resource = repo.getRandomTrackWithPreview()) {
            is Resource.Success -> {
                val track = resource.data
                Log.d("GameViewModel", "Track Loaded: ${track.title}")
                _state.update { currentState ->
                    currentState.copy(
                        track = track,
                        isLoading = false,
                        error = null,
                        // Reset game-specific state
                        guesses = emptyList(),
                        currentAttemptIndex = 0,
                        currentGuess = "",
                        gameFinished = false,
                        gameWon = false
                    )
                }

                track.previewUrl?.let { url ->
                    player.play(url)
                } ?: run {
                    _state.update { it.copy(error = "Track found but preview URL is missing.", isLoading = false) }
                }
            }

            is Resource.Error -> {
                Log.e("GameViewModel", "Error loading track: ${resource.message}")
                _state.update { it.copy(error = resource.message, isLoading = false) }
            }

            Resource.Loading -> {
                // Handled by initial isLoading = true
            }
        }
    }

    // We might need a function to reset the game state to play again
    fun resetGame() {
        player.stop()
        _state.value = GameUiState() // Reset to initial state, ready for play button
    }


    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}