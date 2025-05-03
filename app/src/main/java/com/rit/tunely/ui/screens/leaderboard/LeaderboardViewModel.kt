package com.rit.tunely.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.repository.LeaderboardRepository
import com.rit.tunely.data.state.LeaderboardUiState
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepo: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardUiState())
    val state: StateFlow<LeaderboardUiState> = _state


    init {
        fetchLeaderboard()
    }

    fun fetchLeaderboard() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        when (val res = leaderboardRepo.fetchTopUsers()) {
            is Resource.Success -> _state.value = _state.value.copy(users = res.data)
            is Resource.Error -> _state.value = _state.value.copy(error = res.message)
            else -> {}
        }
        _state.value = _state.value.copy(isLoading = false)
    }
}
