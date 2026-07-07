package com.sharjeel.newsapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.sharjeel.newsapp.domain.model.User

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser?>
    suspend fun signup(email: String, password: String): Result<FirebaseUser?>
    suspend fun saveUserProfile(user: User): Result<Unit>
    suspend fun getUserProfile(uid: String): Result<User?>
    suspend fun getEmailFromPhone(phoneNumber: String): Result<String?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun signInWithFacebook(accessToken: String): Result<FirebaseUser?>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun verifyPhoneNumber(
        phoneNumber: String, 
        activity: android.app.Activity, 
        onCodeSent: (String) -> Unit,
        onFailure: (String) -> Unit
    ): Result<Unit>
    suspend fun signInWithPhoneCredential(verificationId: String, otp: String): Result<com.google.firebase.auth.FirebaseUser?>
    fun logout()
    fun isUserLoggedIn(): Boolean
}
