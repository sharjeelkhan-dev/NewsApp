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

    private val _aiSummary = mutableStateOf<String?>(null)
    val aiSummary: State<String?> = _aiSummary

    private val _aiTranslation = mutableStateOf<String?>(null)
    val aiTranslation: State<String?> = _aiTranslation

    private val _aiSentiment = mutableStateOf<String?>(null)
    val aiSentiment: State<String?> = _aiSentiment

    private val _aiEnhancedHeadline = mutableStateOf<String?>(null)
    val aiEnhancedHeadline: State<String?> = _aiEnhancedHeadline

    // Granular Loading States
    private val _isSummarizing = mutableStateOf(false)
    val isSummarizing: State<Boolean> = _isSummarizing

    private val _isTranslating = mutableStateOf(false)
    val isTranslating: State<Boolean> = _isTranslating

    private val _isAnalyzingSentiment = mutableStateOf(false)
    val isAnalyzingSentiment: State<Boolean> = _isAnalyzingSentiment

    private val _isEnhancingHeadline = mutableStateOf(false)
    val isEnhancingHeadline: State<Boolean> = _isEnhancingHeadline

    fun setArticle(article: Article) {
        _article.value = article
        // Reset AI states when a new article is set
        _aiSummary.value = null
        _aiTranslation.value = null
        _aiSentiment.value = null
        _aiEnhancedHeadline.value = null
        
        // Reset loading states
        _isSummarizing.value = false
        _isTranslating.value = false
        _isAnalyzingSentiment.value = false
        _isEnhancingHeadline.value = false
    }

    fun enhanceHeadline(title: String) {
        viewModelScope.launch {
            _isEnhancingHeadline.value = true
            newsRepository.enhanceHeadline(title).onSuccess {
                _aiEnhancedHeadline.value = it
            }
            _isEnhancingHeadline.value = false
        }
    }

    fun summarizeArticle(content: String) {
        viewModelScope.launch {
            _isSummarizing.value = true
            newsRepository.summarizeArticle(content).onSuccess {
                _aiSummary.value = it
            }
            _isSummarizing.value = false
        }
    }

    fun translateArticle(content: String, language: String) {
        viewModelScope.launch {
            _isTranslating.value = true
            newsRepository.translateArticle(content, language).onSuccess {
                _aiTranslation.value = it
            }
            _isTranslating.value = false
        }
    }

    fun analyzeSentiment(content: String) {
        viewModelScope.launch {
            _isAnalyzingSentiment.value = true
            newsRepository.analyzeSentiment(content).onSuccess {
                _aiSentiment.value = it
            }
            _isAnalyzingSentiment.value = false
        }
    }
}
