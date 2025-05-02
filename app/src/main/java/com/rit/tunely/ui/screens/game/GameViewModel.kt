package com.rit.tunely.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.model.Track
import com.rit.tunely.data.repository.SpotifyRepository
import com.rit.tunely.player.AudioPlayer
import com.rit.tunely.util.Constants
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val track: Track? = null,
    val maskedTitle: String = "",
    val attemptsLeft: Int = Constants.GUESSES_TOTAL,
    val currentGuess: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: SpotifyRepository,
    private val player: AudioPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state
    private lateinit var token: String

    fun init(token: String) {
        this.token = token;
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

    private fun loadTrack() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        when (val randomTrack = repo.getRandomTrack()) {
            is Resource.Success -> {
                val track = randomTrack.data
                _state.value = GameUiState(
                    track = track,
                    maskedTitle = mask(track.title),
                    attemptsLeft = Constants.GUESSES_TOTAL
                )
                track.previewUrl?.let { player.play(it) }
            }

            is Resource.Error -> _state.update { it.copy(error = randomTrack.message) }
            else -> {}
        }
        _state.update { it.copy(isLoading = false) }
    }


    private fun mask(t: String) = t.uppercase().map { if (it.isLetter()) '_' else it }.joinToString(" ")
}