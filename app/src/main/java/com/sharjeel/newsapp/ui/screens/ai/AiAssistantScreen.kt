package com.sharjeel.newsapp.ui.screens.ai

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Composable
fun AiAssistantScreen(
    onBackClick: () -> Unit,
    viewModel: AiAssistantViewModel = hiltViewModel()
) {
    var textState by remember { mutableStateOf("") }
    val messages = viewModel.messages
    val isLoading by viewModel.isLoading
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    AiAssistantScreenContent(
        messages = messages,
        isLoading = isLoading,
        textState = textState,
        onTextChange = { textState = it },
        onSendMessage = {
            if (textState.isNotBlank()) {
                viewModel.sendMessage(textState)
                textState = ""
            }
        },
        onBackClick = onBackClick,
        listState = listState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreenContent(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    textState: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onBackClick: () -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Akhbar AI Assistant",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
                        )
                    )
                )
        ) {
            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { message ->
                    EnhancedChatBubble(message)
                }
                if (isLoading) {
                    item {
                        ThinkingIndicator()
                    }
                }
            }

            // Sleek Floating Input Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                val buttonBgColor by animateColorAsState(
                    targetValue = if (textState.isNotBlank()) BluePrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    label = "buttonBgColor"
                )

                val buttonIconColor by animateColorAsState(
                    targetValue = if (textState.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    label = "buttonIconColor"
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(28.dp),
                            spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(28.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = textState,
                            onValueChange = onTextChange,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp,
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(BluePrimary),
                            maxLines = 4,
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    if (textState.isEmpty()) {
                                        Text(
                                            text = "Ask anything about news...",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = onSendMessage,
                            enabled = textState.isNotBlank(),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(buttonBgColor)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                modifier = Modifier.size(18.dp),
                                tint = buttonIconColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    val bgColor = if (isUser) BluePrimary else MaterialTheme.colorScheme.surface
    val contentColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface

    val bubbleShape = if (isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            modifier = if (isUser) {
                Modifier.widthIn(max = 280.dp)
            } else {
                Modifier.fillMaxWidth()
            }.shadow(
                elevation = if (isUser) 2.dp else 1.dp,
                shape = bubbleShape,
                spotColor = if (isUser) BluePrimary.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.05f)
            ),
            shape = bubbleShape,
            color = bgColor,
            contentColor = contentColor
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 23.sp,
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun ThinkingIndicator() {
    Row(
        modifier = Modifier
            .padding(start = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = BluePrimary.copy(alpha = 0.1f)),
            modifier = Modifier.size(32.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = BluePrimary
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Thinking...",
            style = MaterialTheme.typography.labelMedium.copy(
                color = BluePrimary,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

// =====================================================================
// PREVIEWS
// =====================================================================

@Preview(name = "User Bubble - Light", showBackground = true)
@Composable
private fun EnhancedChatBubbleUserPreview() {
    NewsAppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            EnhancedChatBubble(
                message = ChatMessage(
                    text = "Can you summarize today's headlines?",
                    isUser = true
                )
            )
        }
    }
}

@Preview(name = "AI Bubble - Light", showBackground = true)
@Composable
private fun EnhancedChatBubbleAiPreview() {
    NewsAppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            EnhancedChatBubble(
                message = ChatMessage(
                    text = "Here are the top headlines:\n- Global tech updates\n- Financial market trends",
                    isUser = false
                )
            )
        }
    }
}

@Preview(name = "Thinking Indicator - Light", showBackground = true)
@Composable
private fun ThinkingIndicatorPreview() {
    NewsAppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ThinkingIndicator()
        }
    }
}

@Preview(name = "Screen - Light Mode", showBackground = true)
@Composable
private fun AiAssistantScreenLightPreview() {
    NewsAppTheme(darkTheme = false) {
        AiAssistantScreenContent(
            messages = listOf(
                ChatMessage("Hello! How can I assist you with the news today?", isUser = false),
                ChatMessage("Give me a quick update on AI technology.", isUser = true),
                ChatMessage("Recent advances focus on edge AI processing and natural language understanding in mobile devices.", isUser = false)
            ),
            isLoading = false,
            textState = "",
            onTextChange = {},
            onSendMessage = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Screen - Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AiAssistantScreenDarkPreview() {
    NewsAppTheme(darkTheme = true) {
        AiAssistantScreenContent(
            messages = listOf(
                ChatMessage("Hello! How can I assist you with the news today?", isUser = false),
                ChatMessage("Summarize the economic news.", isUser = true)
            ),
            isLoading = true,
            textState = "Can you add more details?",
            onTextChange = {},
            onSendMessage = {},
            onBackClick = {}
        )
    }
}