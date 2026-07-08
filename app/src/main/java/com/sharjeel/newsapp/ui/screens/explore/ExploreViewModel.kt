package com.sharjeel.newsapp.ui.screens.explore

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.model.NewsSource
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _sources = mutableStateOf<List<NewsSource>>(emptyList())
    val sources: State<List<NewsSource>> = _sources

    private val _popularNews = mutableStateOf<List<Article>>(emptyList())
    val popularNews: State<List<Article>> = _popularNews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var followedSourceIds = emptyList<String>()

    init {
        observeFollowedSources()
        loadSources()
        loadPopularNews()
    }

    private fun observeFollowedSources() {
        viewModelScope.launch {
            newsRepository.getFollowedSources().collectLatest { followedIds ->
                followedSourceIds = followedIds
                updateSourcesWithFollowStatus()
            }
        }
    }

    fun loadSources() {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getAllSources().onSuccess { allSources ->
                _sources.value = allSources
                updateSourcesWithFollowStatus()
            }.onFailure {
                android.util.Log.e("ExploreVM", "Failed to load sources", it)
            }
            _isLoading.value = false
        }
    }

    fun loadPopularNews() {
        viewModelScope.launch {
            _isLoading.value = true
            // Load more regions for explore screen to make it professional
            newsRepository.getTopHeadlines(category = "general").onSuccess { articles ->
                if (articles.isNotEmpty()) {
                    _popularNews.value = articles
                }
            }.onFailure {
                android.util.Log.e("ExploreVM", "Failed to load popular news", it)
            }
            _isLoading.value = false
        }
    }

    fun toggleFollowSource(source: NewsSource) {
        viewModelScope.launch {
            if (source.isFollowed) {
                newsRepository.unfollowSource(source.id)
            } else {
                newsRepository.followSource(source.id)
            }
        }
    }

    private fun updateSourcesWithFollowStatus() {
        _sources.value = _sources.value.map { source ->
            source.copy(isFollowed = followedSourceIds.contains(source.id))
        }
    }
}
