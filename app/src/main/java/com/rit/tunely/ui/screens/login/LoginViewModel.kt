package com.rit.tunely.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.repository.AuthRepository
import com.rit.tunely.data.state.LoginUiState
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEmailChange(v: String) {
        _state.value = _state.value.copy(email = v)
    }

    fun onPasswordChange(v: String) {
        _state.value = _state.value.copy(password = v)
    }

    fun login(onSuccess: () -> Unit) = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        when (auth.login(_state.value.email, _state.value.password)) {
            is Resource.Success -> onSuccess()
            is Resource.Error -> _state.value = _state.value.copy(error = "Incorrect email or password")
            is Resource.Loading -> {}
        }
        _state.value = _state.value.copy(isLoading = false)
    }
}
