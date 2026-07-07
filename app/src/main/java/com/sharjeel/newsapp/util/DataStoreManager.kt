package com.sharjeel.newsapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "news_app_preferences")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_FINISHED = booleanPreferencesKey("onboarding_finished")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val REMEMBER_ME = booleanPreferencesKey("remember_me")
        val SAVED_EMAIL = stringPreferencesKey("saved_email")
    }

    val rememberMe: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[PreferencesKeys.REMEMBER_ME] ?: false }

    suspend fun saveRememberMe(remember: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REMEMBER_ME] = remember
        }
    }

    val savedEmail: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences[PreferencesKeys.SAVED_EMAIL] ?: "" }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SAVED_EMAIL] = email
        }
    }

    val isOnboardingFinished: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_FINISHED] ?: false
        }

    suspend fun saveOnboardingFinished(finished: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_FINISHED] = finished
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    suspend fun saveLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = loggedIn
        }
    }
}
