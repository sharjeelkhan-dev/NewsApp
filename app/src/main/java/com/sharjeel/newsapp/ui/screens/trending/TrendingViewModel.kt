package com.sharjeel.newsapp.ui.screens.trending

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
class TrendingViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _trendingNews = mutableStateOf<List<Article>>(emptyList())
    val trendingNews: State<List<Article>> = _trendingNews

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadTrendingNews()
    }

    fun loadTrendingNews() {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getTopHeadlines().onSuccess { articles ->
                _trendingNews.value = articles
            }.onFailure {
                android.util.Log.e("TrendingVM", "Failed to load trending news", it)
            }
            _isLoading.value = false
        }
    }
}
