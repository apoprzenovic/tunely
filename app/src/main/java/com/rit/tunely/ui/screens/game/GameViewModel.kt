package com.rit.tunely.ui.screens.game

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

    fun init() {
        loadTrack()
    }

    fun onGuessChange(v: String) {
        _state.value = _state.value.copy(currentGuess = v.uppercase())
    }

    fun submitGuess() {
        val track = _state.value.track ?: return
        if (_state.value.currentGuess.equals(track.title, true)) {
            _state.value = _state.value.copy(maskedTitle = track.title)
            player.stop()
        } else {
            val left = _state.value.attemptsLeft - 1
            _state.value = _state.value.copy(
                attemptsLeft = left,
                maskedTitle = if (left == 0) track.title else _state.value.maskedTitle
            )
            if (left == 0) player.stop()
        }
        _state.value = _state.value.copy(currentGuess = "")
    }

    fun loadTrack() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null, currentGuess = "", maskedTitle = "") }
        player.stop()

        when (val resource = repo.getRandomTrackWithPreview()) {
            is Resource.Success -> {
                val track = resource.data
                _state.update { currentState ->
                    currentState.copy(
                        track = track,
                        maskedTitle = mask(track.title),
                        attemptsLeft = Constants.GUESSES_TOTAL,
                        isLoading = false,
                        error = null
                    )
                }

                track.previewUrl?.let { url ->
                    player.play(url)
                } ?: run {
                    _state.update { it.copy(error = "Track found but preview URL is missing.", isLoading = false) }
                }
            }

            is Resource.Error -> {
                _state.update { it.copy(error = resource.message, isLoading = false) }
            }

            Resource.Loading -> {
                // Handled by initial isLoading = true
            }
        }
    }

    private fun mask(t: String) = t.uppercase().map { if (it.isLetterOrDigit()) '_' else it }.joinToString(" ")

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}