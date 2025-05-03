package com.rit.tunely.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.model.User
import com.rit.tunely.data.repository.AuthRepository
import com.rit.tunely.data.repository.UserRepository
import com.rit.tunely.data.state.SignUpUiState
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state

    fun onEmailChange(v: String) {
        _state.value = _state.value.copy(email = v)
    }

    fun onUsernameChange(v: String) {
        _state.value = _state.value.copy(username = v)
    }

    fun onPasswordChange(v: String) {
        _state.value = _state.value.copy(password = v)
    }

    fun onConfirmPasswordChange(v: String) {
        _state.value = _state.value.copy(confirmPassword = v)
    }

    fun signUp(onSuccess: () -> Unit) {
        if (_state.value.password != _state.value.confirmPassword) {
            _state.value = _state.value.copy(error = "Passwords don't match"); return
        }
        if (_state.value.username.isBlank()) {
            _state.value = _state.value.copy(error = "Username cannot be empty"); return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val authResult = authRepo.signUp(_state.value.email, _state.value.password)) {
                is Resource.Success -> {
                    val uid = authRepo.currentUser()?.uid
                    if (uid != null) {
                        val user = User(
                            uid = uid,
                            username = _state.value.username,
                            email = _state.value.email
                        )
                        when (userRepo.updateUser(user)) {
                            is Resource.Success -> onSuccess()
                            is Resource.Error -> _state.value = _state.value.copy(
                                error = "Account created but profile setup failed"
                            )

                            else -> {}
                        }
                    } else {
                        _state.value = _state.value.copy(error = "Failed to get user ID")
                    }
                }

                is Resource.Error -> _state.value = _state.value.copy(error = authResult.message)
                else -> {}
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}