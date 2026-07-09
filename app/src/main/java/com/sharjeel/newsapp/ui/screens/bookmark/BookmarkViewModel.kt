package com.sharjeel.newsapp.ui.screens.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _bookmarkedArticles = mutableStateOf<List<Article>>(emptyList())
    val bookmarkedArticles: State<List<Article>> = _bookmarkedArticles

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        observeBookmarks()
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.getBookmarkedArticles()
                .catch { e ->
                    android.util.Log.e("BookmarkVM", "Error observing bookmarks", e)
                    _isLoading.value = false
                }
                .collectLatest { articles ->
                    _bookmarkedArticles.value = articles
                    _isLoading.value = false
                }
        }
    }

    fun removeBookmark(articleUrl: String) {
        viewModelScope.launch {
            try {
                newsRepository.removeBookmark(articleUrl)
            } catch (e: Exception) {
                android.util.Log.e("BookmarkVM", "Error removing bookmark", e)
            }
        }
    }
    
    fun bookmarkArticle(article: Article) {
        viewModelScope.launch {
            try {
                newsRepository.bookmarkArticle(article)
            } catch (e: Exception) {
                android.util.Log.e("BookmarkVM", "Error adding bookmark", e)
            }
        }
    }
}
