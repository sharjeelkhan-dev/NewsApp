package com.sharjeel.newsapp.ui.screens.author_profile

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
class AuthorViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _sourceNews = mutableStateOf<List<Article>>(emptyList())
    val sourceNews: State<List<Article>> = _sourceNews

    private val _isFollowing = mutableStateOf(false)
    val isFollowing: State<Boolean> = _isFollowing

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var currentSourceId: String = ""

    fun loadSourceData(sourceId: String) {
        if (currentSourceId == sourceId) return
        currentSourceId = sourceId
        
        checkFollowingStatus(sourceId)
        loadNews(sourceId)
    }

    private fun checkFollowingStatus(sourceId: String) {
        viewModelScope.launch {
            newsRepository.getFollowedSources().collectLatest { followedIds ->
                _isFollowing.value = followedIds.contains(sourceId)
            }
        }
    }

    private fun loadNews(sourceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getNewsBySource(sourceId).onSuccess { articles ->
                _sourceNews.value = articles
            }
            _isLoading.value = false
        }
    }

    fun toggleFollow(sourceId: String) {
        viewModelScope.launch {
            if (_isFollowing.value) {
                newsRepository.unfollowSource(sourceId)
            } else {
                newsRepository.followSource(sourceId)
            }
        }
    }

    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            if (newsRepository.isArticleBookmarked(article.url)) {
                newsRepository.removeBookmark(article.url)
            } else {
                newsRepository.bookmarkArticle(article)
            }
        }
    }
}
