package com.sharjeel.newsapp.ui.screens.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.User
import com.sharjeel.newsapp.domain.repository.AuthRepository
import com.sharjeel.newsapp.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _userState = mutableStateOf<User?>(null)
    val userState: State<User?> = _userState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Step 1: Check for local image first
            val localPath = repository.getLocalProfileImage()
            
            // Step 2: Immediate fetch from Firestore
            val uid = repository.currentUser?.uid
            if (uid != null) {
                repository.getUserProfile(uid).onSuccess { user ->
                    _userState.value = user?.copy(
                        profileImageUrl = localPath ?: user.profileImageUrl
                    )
                    _isLoading.value = false
                }.onFailure {
                    _isLoading.value = false
                }
            } else {
                _isLoading.value = false
            }

            // Step 3: Listen for background updates
            repository.getCurrentUserProfile().collect { user ->
                _userState.value = user?.copy(
                    profileImageUrl = repository.getLocalProfileImage() ?: user.profileImageUrl
                )
            }
        }
    }

    fun loadUserProfile() {
        // Redundant with observeUserProfile, but keeping for compatibility
    }

    fun uploadProfileImage(uri: android.net.Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveLocalProfileImage(uri).onSuccess { path ->
                // Image is saved locally on device
                val currentUser = _userState.value ?: User()
                _userState.value = currentUser.copy(profileImageUrl = path)
            }.onFailure { e ->
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Failed to save image locally"))
            }
            _isLoading.value = false
        }
    }

    fun updateProfile(
        username: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        bio: String,
        website: String
    ) {
        viewModelScope.launch {
            val currentUser = _userState.value ?: return@launch
            val updatedUser = currentUser.copy(
                username = username,
                fullName = fullName,
                email = email,
                phoneNumber = phoneNumber,
                bio = bio,
                website = website
            )
            
            _isLoading.value = true
            repository.saveUserProfile(updatedUser).onSuccess {
                _userState.value = updatedUser
                _eventFlow.emit(UiEvent.ProfileUpdated)
            }.onFailure { e ->
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Failed to update profile"))
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            dataStoreManager.saveLoggedIn(false)
            _eventFlow.emit(UiEvent.LoggedOut)
        }
    }

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        object ProfileUpdated : UiEvent()
        object LoggedOut : UiEvent()
    }
}
