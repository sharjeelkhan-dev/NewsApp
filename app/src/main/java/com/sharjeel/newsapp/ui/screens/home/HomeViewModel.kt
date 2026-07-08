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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    // 1. Pakistan ke top verified news sources ke domains
    private val pakistaniDomains = "dawn.com,tribune.com.pk,geo.tv,arynews.tv,thenews.com.pk"

    private val _userState = mutableStateOf<User?>(null)
    val userState: State<User?> = _userState

    private val _trendingNews = mutableStateOf<List<Article>>(emptyList())
    val trendingNews: State<List<Article>> = _trendingNews

    private val _latestNews = mutableStateOf<List<Article>>(emptyList())
    val latestNews: State<List<Article>> = _latestNews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // 2. Search States for UI Binding
    var searchQuery = mutableStateOf("")
        private set

    private var searchJob: Job? = null

    init {
        loadInitialData()
        observeUserProfile()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                loadTrendingNewsInternal()
                loadLatestNewsInternal("All")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            authRepository.getCurrentUserProfile().collect { user ->
                android.util.Log.d("HomeViewModel", "Profile Synced - Username: ${user?.username}, Topics: ${user?.topics}")

                _userState.value = user?.copy(
                    profileImageUrl = authRepository.getLocalProfileImage() ?: user.profileImageUrl
                )

                user?.topics?.filter { it.isNotBlank() }?.takeIf { it.isNotEmpty() }?.let { userTopics ->
                    android.util.Log.d("HomeViewModel", "Loading news for user-selected topics: $userTopics")
                    loadLatestNews(userTopics)
                }
            }
        }
    }

    private suspend fun loadTrendingNewsInternal() {
        try {
            newsRepository.getTopHeadlines(category = "general").onSuccess { articles ->
                if (articles.isNotEmpty()) {
                    _trendingNews.value = articles
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Trending News API error", e)
        }
    }

    private suspend fun loadLatestNewsInternal(category: String) {
        try {
            newsRepository.getTopHeadlines(category).onSuccess { articles ->
                _latestNews.value = articles.ifEmpty { emptyList() }
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Latest News API error", e)
        }
    }

    fun loadLatestNews(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            loadLatestNewsInternal(category)
            _isLoading.value = false
        }
    }

    fun loadLatestNews(interests: List<String>) {
        if (interests.isEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                newsRepository.getNewsByInterests(interests).onSuccess { articles ->
                    if (articles.isNotEmpty()) {
                        _latestNews.value = articles
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        searchJob?.cancel()

        if (query.isBlank()) {
            loadInitialData()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            _isLoading.value = true
            try {


                // Temporary log to trace trigger
                android.util.Log.d("HomeViewModel", "Searching for: $query in domains: $pakistaniDomains")
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Search API error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}