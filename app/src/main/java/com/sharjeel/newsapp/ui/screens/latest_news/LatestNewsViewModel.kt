package com.sharjeel.newsapp.ui.screens.latest_news

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
class LatestNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _latestNews = mutableStateOf<List<Article>>(emptyList())
    val latestNews: State<List<Article>> = _latestNews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadLatestNews("general")
    }

    fun loadLatestNews(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val cat = if (category == "All") null else category
            newsRepository.getTopHeadlines(category = cat).onSuccess { articles ->
                _latestNews.value = articles
            }.onFailure {
                android.util.Log.e("LatestNewsVM", "Failed to load latest news", it)
            }
            _isLoading.value = false
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
            _latestNews.value = _latestNews.value.filter { it.url != article.url }
        }
    }

    fun blockSource(sourceId: String) {
        viewModelScope.launch {
            newsRepository.blockSource(sourceId)
            _latestNews.value = _latestNews.value.filter { it.sourceId != sourceId }
        }
    }

    fun reportArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.reportArticle(article.url, "Reported from Latest News")
        }
    }
}
