package com.sharjeel.newsapp.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.sharjeel.newsapp.domain.model.User
import com.sharjeel.newsapp.domain.repository.AuthRepository
import com.sharjeel.newsapp.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val savedEmail = dataStoreManager.savedEmail
    val isRememberMeChecked = dataStoreManager.rememberMe

    var signupUser by mutableStateOf(User())
        private set

    private var _verificationId = ""
    private var _pendingPassword = ""

    fun updateSignupData(
        id: String? = null,
        username: String? = null,
        email: String? = null,
        fullName: String? = null,
        phoneNumber: String? = null,
        profileImageUrl: String? = null,
        country: String? = null,
        topics: List<String>? = null,
        sources: List<String>? = null,
        bio: String? = null,
        website: String? = null
    ) {
        signupUser = signupUser.copy(
            id = id ?: signupUser.id,
            username = username ?: signupUser.username,
            email = email ?: signupUser.email,
            fullName = fullName ?: signupUser.fullName,
            phoneNumber = phoneNumber ?: signupUser.phoneNumber,
            profileImageUrl = profileImageUrl ?: signupUser.profileImageUrl,
            country = country ?: signupUser.country,
            topics = topics ?: signupUser.topics,
            sources = sources ?: signupUser.sources,
            bio = bio ?: signupUser.bio,
            website = website ?: signupUser.website
        )
    }

    fun uploadProfileImage(uri: android.net.Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.saveLocalProfileImage(uri).onSuccess { path ->
                updateSignupData(profileImageUrl = path)
            }.onFailure { e ->
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Failed to save image locally"))
            }
            _isLoading.value = false
        }
    }

    fun login(identifier: String, password: String, rememberMe: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Check if identifier is email or phone
            val emailToUse = if (identifier.contains("@")) {
                identifier
            } else {
                // Phone number provided. Check Firestore mapping
                val phoneResult = repository.getEmailFromPhone(identifier)
                var foundEmail: String? = null
                phoneResult.onSuccess { foundEmail = it }
                
                if (foundEmail == null) {
                    _isLoading.value = false
                    _eventFlow.emit(UiEvent.ShowError("No account found with this phone number"))
                    return@launch
                }
                foundEmail!!
            }

            val result = repository.login(emailToUse, password)
            
            result.onSuccess {
                handleSuccessfulLogin(rememberMe, identifier)
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Login failed"))
            }
        }
    }

    fun signup(identifier: String, password: String, rememberMe: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _pendingPassword = password
            
            if (identifier.contains("@")) {
                // Email Signup
                val result = repository.signup(identifier, password)
                result.onSuccess { firebaseUser ->
                    val uid = firebaseUser?.uid ?: ""
                    updateSignupData(id = uid, email = identifier)
                    
                    // Create initial profile in Firestore immediately
                    repository.saveUserProfile(signupUser)

                    handleSuccessfulSignup(rememberMe, identifier)
                }.onFailure { e ->
                    _isLoading.value = false
                    _eventFlow.emit(UiEvent.ShowError(e.message ?: "Signup failed"))
                }
            } else {
                // Phone Signup (Native Provider)
                val cleanPhone = identifier.replace(" ", "").replace("-", "").replace("+", "")
                val formattedPhone = if (!identifier.startsWith("+")) "+$identifier" else identifier
                
                // We'll verify phone first, then create/link account in next step
                repository.verifyPhoneNumber(
                    phoneNumber = formattedPhone,
                    activity = null as? android.app.Activity ?: return@launch, // This will fail if not handled in navigation
                    onCodeSent = { verificationId ->
                        _verificationId = verificationId
                        _isLoading.value = false
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.NavigateToOtp(formattedPhone))
                        }
                    },
                    onFailure = { error ->
                        _isLoading.value = false
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.ShowError(error))
                        }
                    }
                )
            }
        }
    }

    fun handlePhoneSignup(phoneNumber: String, activity: android.app.Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.verifyPhoneNumber(
                phoneNumber = phoneNumber,
                activity = activity,
                onCodeSent = { verificationId ->
                    _verificationId = verificationId
                    _isLoading.value = false
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.NavigateToOtp(phoneNumber))
                    }
                },
                onFailure = { error ->
                    _isLoading.value = false
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowError(error))
                    }
                }
            )
        }
    }

    fun verifySignupOtp(otp: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signInWithPhoneCredential(_verificationId, otp)
            
            result.onSuccess { firebaseUser ->
                val uid = firebaseUser?.uid ?: ""
                val phone = firebaseUser?.phoneNumber ?: ""
                
                // Create a dummy email identity so we can have a password fallback
                val internalEmail = "${phone.replace("+", "")}@newsapp.com"
                try {
                    firebaseUser?.linkWithCredential(
                        EmailAuthProvider.getCredential(internalEmail, _pendingPassword)
                    )?.await()
                } catch (e: Exception) {
                    // Link might fail if user already exists, but we have the phone identity
                }
                
                updateSignupData(id = uid, phoneNumber = phone, email = internalEmail)
                
                // Create initial profile in Firestore
                repository.saveUserProfile(signupUser)

                _isLoading.value = false
                _eventFlow.emit(UiEvent.NavigateToOnboarding)
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "OTP Verification failed"))
            }
        }
    }

    private suspend fun handleSuccessfulLogin(rememberMe: Boolean, identifier: String) {
        if (rememberMe) {
            dataStoreManager.saveEmail(identifier)
            dataStoreManager.saveRememberMe(true)
        }
        dataStoreManager.saveOnboardingFinished(true)
        _isLoading.value = false
        _eventFlow.emit(UiEvent.NavigateToHome)
    }

    private suspend fun handleSuccessfulSignup(rememberMe: Boolean, identifier: String) {
        if (rememberMe) {
            dataStoreManager.saveEmail(identifier)
            dataStoreManager.saveRememberMe(true)
        }
        _isLoading.value = false
        _eventFlow.emit(UiEvent.NavigateToOnboarding)
    }

    fun completeSignup() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.saveUserProfile(signupUser)
            
            result.onSuccess {
                dataStoreManager.saveOnboardingFinished(true)
                dataStoreManager.saveLoggedIn(true) // Ensure user is marked as logged in
                _isLoading.value = false
                _eventFlow.emit(UiEvent.NavigateToHome)
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Failed to save profile"))
            }
        }
    }

    fun saveSignupProgress() {
        viewModelScope.launch {
            if (signupUser.id.isNotEmpty()) {
                repository.saveUserProfile(signupUser)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signInWithGoogle(idToken)
            result.onSuccess { firebaseUser ->
                val uid = firebaseUser?.uid ?: ""
                
                // Check if user already exists in Firestore
                repository.getUserProfile(uid).onSuccess { existingUser ->
                    if (existingUser != null) {
                        // User exists, just go Home
                        dataStoreManager.saveOnboardingFinished(true)
                        dataStoreManager.saveLoggedIn(true)
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.NavigateToHome)
                    } else {
                        // New User - Setup initial data and go to Onboarding
                        updateSignupData(
                            id = uid,
                            email = firebaseUser?.email ?: "",
                            fullName = firebaseUser?.displayName ?: "",
                            profileImageUrl = firebaseUser?.photoUrl?.toString() ?: ""
                        )
                        // Save initial profile immediately to prevent permission issues later
                        repository.saveUserProfile(signupUser)
                        
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.NavigateToOnboarding)
                    }
                }.onFailure {
                    // Fallback for check failure
                    _isLoading.value = false
                    _eventFlow.emit(UiEvent.ShowError("Failed to verify user profile"))
                }
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError("Google Sign-In failed"))
            }
        }
    }

    fun signInWithFacebook(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signInWithFacebook(accessToken)
            result.onSuccess { firebaseUser ->
                val uid = firebaseUser?.uid ?: ""
                
                repository.getUserProfile(uid).onSuccess { existingUser ->
                    if (existingUser != null) {
                        dataStoreManager.saveOnboardingFinished(true)
                        dataStoreManager.saveLoggedIn(true)
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.NavigateToHome)
                    } else {
                        updateSignupData(
                            id = uid,
                            email = firebaseUser?.email ?: "",
                            fullName = firebaseUser?.displayName ?: "",
                            profileImageUrl = firebaseUser?.photoUrl?.toString() ?: ""
                        )
                        repository.saveUserProfile(signupUser)
                        
                        _isLoading.value = false
                        _eventFlow.emit(UiEvent.NavigateToOnboarding)
                    }
                }.onFailure {
                    _isLoading.value = false
                    _eventFlow.emit(UiEvent.ShowError("Failed to verify user profile"))
                }
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError("Facebook Sign-In failed"))
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.sendPasswordResetEmail(email)
            _isLoading.value = false
            result.onSuccess {
                _eventFlow.emit(UiEvent.ShowMessage("Password reset email sent!"))
                _eventFlow.emit(UiEvent.NavigateToCongratulations)
            }.onFailure { e ->
                _eventFlow.emit(UiEvent.ShowError(e.message ?: "Failed to send reset email"))
            }
        }
    }

    fun sendOtp(phoneNumber: String, activity: android.app.Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            val formattedPhone = if (!phoneNumber.startsWith("+")) "+$phoneNumber" else phoneNumber
            repository.verifyPhoneNumber(
                phoneNumber = formattedPhone, 
                activity = activity, 
                onCodeSent = { verificationId ->
                    _verificationId = verificationId
                    _isLoading.value = false
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.NavigateToOtp(formattedPhone))
                    }
                },
                onFailure = { error ->
                    _isLoading.value = false
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowError(error))
                    }
                }
            )
        }
    }

    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signInWithPhoneCredential(_verificationId, otp)
            result.onSuccess {
                _eventFlow.emit(UiEvent.NavigateToResetPassword)
            }.onFailure { e ->
                _isLoading.value = false
                _eventFlow.emit(UiEvent.ShowError("Invalid OTP"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class ShowMessage(val message: String) : UiEvent()
        object NavigateToHome : UiEvent()
        object NavigateToOnboarding : UiEvent()
        object NavigateToCongratulations : UiEvent()
        object NavigateToResetPassword : UiEvent()
        data class NavigateToOtp(val phoneNumber: String) : UiEvent()
    }
}
