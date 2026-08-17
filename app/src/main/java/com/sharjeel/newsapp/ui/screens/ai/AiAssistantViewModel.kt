package com.sharjeel.newsapp.ui.screens.ai

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharjeel.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        _messages.add(ChatMessage("Hello! I'm Akhbar's AI Assistant. How can I help you with the news today?", false))
    }

    fun sendMessage(query: String) {
        if (query.isBlank()) return

        _messages.add(ChatMessage(query, true))
        
        viewModelScope.launch {
            _isLoading.value = true
            newsRepository.askAiAssistant(query).onSuccess { response ->
                _messages.add(ChatMessage(response, false))
            }.onFailure { error ->
                _messages.add(ChatMessage("Sorry, I encountered an error: ${error.message}", false))
            }
            _isLoading.value = false
        }
    }
}
