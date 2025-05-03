package com.rit.tunely.data.state

import com.rit.tunely.data.model.User

data class LeaderboardUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)