package com.sharjeel.newsapp.ui.screens.latest_news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.NewsActionsBottomSheet
import com.sharjeel.newsapp.ui.screens.home.CategoryTabs
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestNewsScreen(
    onBackClick: () -> Unit,
    onNewsItemClick: (String) -> Unit = { _ -> },
    viewModel: LatestNewsViewModel = hiltViewModel()
) {
    val news by viewModel.latestNews
    val isLoading by viewModel.isLoading
    val categories = listOf("All", "Sports", "Politics", "Business", "Health", "Travel", "Science")
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedArticleForActions by remember { mutableStateOf<com.sharjeel.newsapp.domain.model.Article?>(null) }

    AppScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Latest",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
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
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    viewModel.loadLatestNews(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading && news.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = com.sharjeel.newsapp.ui.theme.BluePrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(news) { article ->
                        NewsItem(
                            category = article.sourceName,
                            title = article.title,
                            publisher = article.sourceName,
                            publishedAt = TimeUtils.formatRelativeTime(article.publishedAt),
                            image = R.drawable.newsimages,
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
                viewModel.bookmarkArticle(article)
            },
            onHideClick = {
                viewModel.hideArticle(article)
            },
            onBlockSourceClick = {
                viewModel.blockSource(article.sourceId)
            },
            onReportClick = {
                viewModel.reportArticle(article)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LatestNewsScreenPreview() {
    NewsAppTheme {
        LatestNewsScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LatestNewsScreenDarkPreview() {
    NewsAppTheme {
        LatestNewsScreen(onBackClick = {})
    }
}
