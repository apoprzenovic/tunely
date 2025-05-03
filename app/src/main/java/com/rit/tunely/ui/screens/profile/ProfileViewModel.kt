package com.rit.tunely.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rit.tunely.data.repository.UserRepository
import com.rit.tunely.data.state.ProfileUiState
import com.rit.tunely.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    fun loadProfile(uid: String) = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        when (val res = userRepo.getUser(uid)) {
            is Resource.Success -> _state.value = _state.value.copy(user = res.data)
            is Resource.Error -> _state.value = _state.value.copy(error = res.message)
            else -> {}
        }
        _state.value = _state.value.copy(isLoading = false)
    }

    fun updateBio(newBio: String) {
        _state.value = _state.value.copy(user = _state.value.user.copy(bio = newBio))
    }

    fun saveProfile() = viewModelScope.launch {
        userRepo.updateUser(_state.value.user)
    }
}
