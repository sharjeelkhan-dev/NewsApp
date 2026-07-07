package com.sharjeel.newsapp.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.User
import com.sharjeel.newsapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _userState = mutableStateOf<User?>(null)
    val userState: State<User?> = _userState

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            repository.getCurrentUserProfile().collect { user ->
                _userState.value = user?.copy(
                    profileImageUrl = repository.getLocalProfileImage() ?: user.profileImageUrl
                )
            }
        }
    }
}
