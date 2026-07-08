package com.sharjeel.newsapp.ui.screens.details

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
class DetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _article = mutableStateOf<Article?>(null)
    val article: State<Article?> = _article

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    /**
     * In a real app, you would fetch by ID or URL. 
     * For this implementation, we use a shared state or pass via navigation.
     */
    fun setArticle(article: Article) {
        _article.value = article
    }
}
