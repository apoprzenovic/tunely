package com.rit.tunely.ui.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.repository.AuthRepository
import com.rit.tunely.data.repository.ITunesRepository
import com.rit.tunely.data.repository.UserRepository
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
import kotlin.math.roundToInt

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: ITunesRepository,
    private val player: AudioPlayer,
    private val userRepository: UserRepository,
    private val auth: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state

    fun appendToGuess(char: Char) {
        val currentTrackTitle = _state.value.track?.title ?: return
        if (_state.value.currentGuess.length < currentTrackTitle.length) {
            _state.update { it.copy(currentGuess = it.currentGuess + char) }
        }
    }

    fun deleteLastChar() {
        if (_state.value.currentGuess.isNotEmpty()) {
            _state.update { it.copy(currentGuess = it.currentGuess.dropLast(1)) }
        }
    }

    fun submitGuess() {
        val currentState = _state.value
        val track = currentState.track ?: return
        val guess = currentState.currentGuess.uppercase()

        if (guess.length != track.title.length) {
            Log.w("GameViewModel", "Attempted to submit guess with incorrect length.")
            return
        }

        val updatedGuesses = currentState.guesses + guess
        val guessIndex = currentState.currentAttemptIndex
        val nextAttemptIndex = currentState.currentAttemptIndex + 1

        val gameWon = guess.equals(track.title, ignoreCase = true)
        val attemptsExhausted = nextAttemptIndex >= Constants.GUESSES_TOTAL
        val gameFinished = gameWon || attemptsExhausted

        var pointsToAdd: Int? = null
        if (gameWon) {
            val pointsMultiplier = when (guessIndex) {
                0 -> 1.0
                1 -> 0.9
                2 -> 0.8
                3 -> 0.7
                4 -> 0.6
                5 -> 0.5
                else -> 0.0
            }
            pointsToAdd = (10 * pointsMultiplier).roundToInt()
            if (pointsToAdd <= 0) pointsToAdd = null
        }

        if (gameFinished) {
            player.stop()
        }

        _state.update {
            it.copy(
                guesses = updatedGuesses,
                currentAttemptIndex = nextAttemptIndex,
                currentGuess = "",
                gameFinished = gameFinished,
                gameWon = gameWon,
                pointsEarnedThisRound = pointsToAdd
            )
        }
        Log.d("GameViewModel", "Guess Submitted: $guess, Attempt: $nextAttemptIndex, Won: $gameWon, Finished: $gameFinished")
        Log.d("GameViewModel", "Updated Guesses: $updatedGuesses")

        if (gameWon && pointsToAdd != null) {
            viewModelScope.launch {
                updateUserPoints(pointsToAdd)
            }
        }
    }

    private suspend fun updateUserPoints(pointsToAdd: Int) {
        val currentUser = auth.currentUser() ?: run {
            Log.e("GameViewModel", "Cannot update points: User not logged in.")
            return
        }
        val uid = currentUser.uid

        Log.d("GameViewModel", "Attempting to add $pointsToAdd points for user $uid.")

        when (val userResource = userRepository.getUser(uid)) {
            is Resource.Success -> {
                val user = userResource.data
                val updatedUser = user.copy(points = user.points + pointsToAdd)

                when (val updateResource = userRepository.updateUser(updatedUser)) {
                    is Resource.Success -> {
                        Log.d("GameViewModel", "User points updated successfully for $uid. New total: ${updatedUser.points}")
                    }

                    is Resource.Error -> {
                        Log.e("GameViewModel", "Failed to update user points for $uid: ${updateResource.message}")
                    }

                    Resource.Loading -> {}
                }
            }

            is Resource.Error -> {
                Log.e("GameViewModel", "Failed to fetch user $uid to update points: ${userResource.message}")
            }

            Resource.Loading -> {}
        }
    }

    fun loadTrack() = viewModelScope.launch {
        _state.update {
            GameUiState(isLoading = true)
        }
        player.stop()

        when (val resource = repo.getRandomTrackWithPreview()) {
            is Resource.Success -> {
                val track = resource.data
                Log.d("GameViewModel", "Track Loaded: ${track.title}")
                _state.update { currentState ->
                    currentState.copy(
                        track = track,
                        isLoading = false,
                        error = null,
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

    fun resetGame() {
        player.stop()
        _state.value = GameUiState()
    }


    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}