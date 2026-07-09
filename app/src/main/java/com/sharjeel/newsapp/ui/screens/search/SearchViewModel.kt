package com.sharjeel.newsapp.ui.screens.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _searchResults = mutableStateOf<List<Article>>(emptyList())
    val searchResults: State<List<Article>> = _searchResults

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun searchNews(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getNewsByInterests(listOf(query)).onSuccess { articles ->
                _searchResults.value = articles
            }.onFailure {
                android.util.Log.e("SearchVM", "Search failed", it)
            }
            _isLoading.value = false
        }
    }

    fun followSource(sourceId: String) {
        viewModelScope.launch {
            newsRepository.followSource(sourceId)
        }
    }

    fun unfollowSource(sourceId: String) {
        viewModelScope.launch {
            newsRepository.unfollowSource(sourceId)
        }
    }

    fun bookmarkArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.bookmarkArticle(article)
        }
    }

    fun hideArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.hideArticle(article.url)
            _searchResults.value = _searchResults.value.filter { it.url != article.url }
        }
    }

    fun blockSource(sourceId: String) {
        viewModelScope.launch {
            newsRepository.blockSource(sourceId)
            _searchResults.value = _searchResults.value.filter { it.sourceId != sourceId }
        }
    }

    fun reportArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.reportArticle(article.url, "Reported from Search")
        }
    }
}
