package com.sharjeel.newsapp.ui.screens.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.NewsActionsBottomSheet
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.util.SmoothScrollConfig

@Composable
fun BookmarkScreen(
    onNewsItemClick: (String) -> Unit = { _ -> },
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarkedArticles by viewModel.bookmarkedArticles
    val isLoading by viewModel.isLoading
    
    var searchQuery by remember { mutableStateOf("") }
    val filteredArticles = remember(bookmarkedArticles, searchQuery) {
        if (searchQuery.isBlank()) bookmarkedArticles
        else bookmarkedArticles.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()
    var selectedArticleForActions by remember { mutableStateOf<com.sharjeel.newsapp.domain.model.Article?>(null) }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bookmark",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Search in bookmarks",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.magnifying_glass_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading && bookmarkedArticles.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (filteredArticles.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.saved_bookmark_icon),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isEmpty()) "No bookmarks yet" else "No matching bookmarks",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Bookmarked News List
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    state = listState,
                    flingBehavior = flingBehavior,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredArticles, key = { it.url }) { article ->
                        NewsItem(
                            category = article.sourceName,
                            title = article.title,
                            publisher = article.sourceName,
                            publishedAt = article.publishedAt,
                            image = R.drawable.newsimages2,
                            remoteImageUrl = article.urlToImage,
                            articleUrl = article.url,
                            onItemClick = { onNewsItemClick(article.url) },
                            onActionsClick = { selectedArticleForActions = article }
                        )
                    }
                }
            }
        }
    }

    selectedArticleForActions?.let { article ->
        NewsActionsBottomSheet(
            onDismissRequest = { selectedArticleForActions = null },
            articleTitle = article.title,
            articleUrl = article.url,
            sourceName = article.sourceName,
            onBookmarkClick = {
                viewModel.removeBookmark(article.url)
            },
            isAlreadyBookmarked = true
        )
    }
}
