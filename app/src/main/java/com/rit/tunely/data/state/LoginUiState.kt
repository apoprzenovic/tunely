package com.rit.tunely.data.state

data class LoginUiState(
    val email: String = "", val password: String = "",
    val isLoading: Boolean = false, val error: String? = null
)