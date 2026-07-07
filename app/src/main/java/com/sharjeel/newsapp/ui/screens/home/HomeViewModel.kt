package com.sharjeel.newsapp.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.model.User
import com.sharjeel.newsapp.domain.repository.AuthRepository
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _userState = mutableStateOf<User?>(null)
    val userState: State<User?> = _userState

    private val _trendingNews = mutableStateOf<List<Article>>(emptyList())
    val trendingNews: State<List<Article>> = _trendingNews

    private val _latestNews = mutableStateOf<List<Article>>(emptyList())
    val latestNews: State<List<Article>> = _latestNews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        observeUserProfile()
        loadTrendingNews()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            authRepository.getCurrentUserProfile().collect { user ->
                _userState.value = user?.copy(
                    profileImageUrl = authRepository.getLocalProfileImage() ?: user.profileImageUrl
                )
                // Load personalized news once user interests are available
                // If user has no topics, use some default categories to ensure news is shown
                val interests = user?.topics?.takeIf { it.isNotEmpty() } ?: listOf("General", "Technology", "World")
                loadLatestNews(interests)
            }
        }
    }

    fun loadTrendingNews() {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getTopHeadlines().onSuccess { articles ->
                _trendingNews.value = articles
            }
            _isLoading.value = false
        }
    }

    fun loadLatestNews(interests: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getNewsByInterests(interests).onSuccess { articles ->
                _latestNews.value = articles
            }
            _isLoading.value = false
        }
    }
}