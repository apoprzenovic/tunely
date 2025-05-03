package com.rit.tunely.data.state

import com.rit.tunely.data.model.User

data class ProfileUiState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val error: String? = null
)