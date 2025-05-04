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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: ITunesRepository,
    private val player: AudioPlayer,
    private val users: UserRepository,
    private val auth: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState(isPlaying = player.isPlaying))
    val state: StateFlow<GameUiState> = _state

    private fun String.titleWithoutSpaces() = this.replace(" ", "")

    fun appendToGuess(c: Char) = _state.update {
        if (c == ' ') return@update it

        val title = it.track?.title ?: return@update it
        if (it.currentGuess.length < title.titleWithoutSpaces().length) {
            it.copy(currentGuess = it.currentGuess + c)
        } else {
            it
        }
    }

    fun deleteLastChar() = _state.update {
        if (it.currentGuess.isNotEmpty()) it.copy(currentGuess = it.currentGuess.dropLast(1)) else it
    }

    fun submitGuess() {
        val st = _state.value
        val track = st.track ?: return
        val guess = st.currentGuess.uppercase()
        val titleWithoutSpaces = track.title.titleWithoutSpaces()

        if (guess.length != titleWithoutSpaces.length) {
            Log.w(TAG, "Guess length (${guess.length}) doesn't match title without spaces length (${titleWithoutSpaces.length})")
            return
        }

        val newGuesses = st.guesses + guess
        val attemptIdx = st.currentAttemptIndex

        val won = guess.equals(titleWithoutSpaces, ignoreCase = true)
        val finished = won || attemptIdx + 1 >= Constants.GUESSES_TOTAL
        val points = pointsTable[attemptIdx]?.takeIf { won }

        if (finished) player.stop()

        _state.update {
            it.copy(
                guesses = newGuesses,
                currentAttemptIndex = attemptIdx + 1,
                currentGuess = "",
                gameFinished = finished,
                gameWon = won,
                pointsEarnedThisRound = points
            )
        }

        points?.let { viewModelScope.launch { addUserPoints(it) } }
    }

    private val pointsTable = mapOf(
        0 to 10, 1 to 9, 2 to 8, 3 to 7, 4 to 6, 5 to 5
    )

    private suspend fun addUserPoints(p: Int) {
        val uid = auth.currentUser()?.uid ?: return
        when (val res = users.getUser(uid)) {
            is Resource.Success -> users.updateUser(res.data.copy(points = res.data.points + p))
            is Resource.Error -> Log.e(TAG, "Fetch user failed: ${res.message}")
            Resource.Loading -> {}
        }
    }

    fun loadTrack() = viewModelScope.launch {
        _state.value = GameUiState(isLoading = true, isPlaying = player.isPlaying)
        player.stop()

        when (val res = repo.getRandomTrackWithPreview()) {
            is Resource.Success -> res.data.let { track ->
                _state.value = GameUiState(track = track, isPlaying = player.isPlaying)
                track.previewUrl?.let(player::play)
                    ?: _state.update { it.copy(error = "Preview URL missing") }
            }

            is Resource.Error -> _state.update { it.copy(error = res.message) }
            Resource.Loading -> {}
        }
    }

    fun togglePlayPause() = viewModelScope.launch {
        val currentTrack = _state.value.track
        val isCurrentlyPlaying = player.isPlaying.first()

        if (isCurrentlyPlaying) {
            player.stop()
        } else {
            currentTrack?.previewUrl?.let { player.play(it) }
        }
    }

    fun resetGame() {
        player.stop()
        _state.value = GameUiState(isPlaying = player.isPlaying)
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

    private companion object {
        const val TAG = "GameViewModel"
    }
}