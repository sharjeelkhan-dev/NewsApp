package com.sharjeel.newsapp.ui.screens.latest_news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.screens.home.CategoryTabs
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LatestNewsScreen(
    onBackClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    val categories = listOf("All", "Sports", "Politics", "Business", "Health", "Travel", "Science")
    var selectedCategory by remember { mutableStateOf("All") }

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
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    NewsItem(
                        category = "Europe",
                        title = "Ukraine's President Zelenskyy to BBC: Blood money being paid for Russian oil",
                        publisher = "BBC News",
                        time = "14m ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Navigate to Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Travel",
                        title = "Her train broke down. Her phone died. And then she met her future husband",
                        publisher = "CNN",
                        time = "1h ago",
                        image = R.drawable.newsimages3,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Navigate to Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Europe",
                        title = "Russian warship: Moskva sinks in Black Sea",
                        publisher = "BBC News",
                        time = "4h ago",
                        image = R.drawable.newsimages,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Navigate to Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Money",
                        title = "Wind power produced more electricity than coal and nuclear combined",
                        publisher = "USA Today",
                        time = "4h ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Navigate to Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Life",
                        title = "'We keep rising to new challenges:' For churches hit by",
                        publisher = "USA Today",
                        time = "4h ago",
                        image = R.drawable.newsimages3,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Navigate to Detail */ }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LatestNewsScreenPreview() {
    NewsAppTheme {
        LatestNewsScreen(onBackClick = {}, onAuthorClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LatestNewsScreenDarkPreview() {
    NewsAppTheme {
        LatestNewsScreen(onBackClick = {}, onAuthorClick = {})
    }
}
