package com.sharjeel.newsapp.ui.screens.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.components.FilterBottomSheet
import com.sharjeel.newsapp.ui.components.NewsActionsBottomSheet
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.TimeUtils

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onNewsItemClick: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchResults
    val isLoading by viewModel.isLoading

    SearchScreenContent(
        searchResults = searchResults,
        isLoading = isLoading,
        onSearch = { viewModel.searchNews(it) },
        onFollowSource = { viewModel.followSource(it) },
        onUnfollowSource = { viewModel.unfollowSource(it) },
        onBookmarkArticle = { viewModel.bookmarkArticle(it) },
        onHideArticle = { viewModel.hideArticle(it) },
        onBlockSource = { viewModel.blockSource(it) },
        onReportArticle = { viewModel.reportArticle(it) },
        onBackClick = onBackClick,
        onNewsItemClick = onNewsItemClick
    )
}

@Composable
fun SearchScreenContent(
    searchResults: List<com.sharjeel.newsapp.domain.model.Article>,
    isLoading: Boolean,
    onSearch: (String) -> Unit,
    onFollowSource: (String) -> Unit,
    onUnfollowSource: (String) -> Unit,
    onBookmarkArticle: (com.sharjeel.newsapp.domain.model.Article) -> Unit,
    onHideArticle: (com.sharjeel.newsapp.domain.model.Article) -> Unit,
    onBlockSource: (String) -> Unit,
    onReportArticle: (com.sharjeel.newsapp.domain.model.Article) -> Unit,
    onBackClick: () -> Unit,
    onNewsItemClick: (String) -> Unit,
    onActionsClick: (com.sharjeel.newsapp.domain.model.Article) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedArticleForActions by remember { mutableStateOf<com.sharjeel.newsapp.domain.model.Article?>(null) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("News", "Topics", "Author")

    val recentSearches = remember { mutableStateListOf("Europe", "Zelenskyy", "Russian Oil", "Future husband") }

    // Managed states for topics and authors
    val topicsState = remember {
        mutableStateListOf(
            TopicItemData("Health", "View the latest health news and explore articles on...", R.drawable.newsimages2, false),
            TopicItemData("Technology", "The latest tech news about the world's best hardware...", R.drawable.newsimages3, true),
            TopicItemData("Art", "The Art Newspaper is the journal of record for...", R.drawable.newsimages, true),
            TopicItemData("Politics", "Opinion and analysis of American and global politics...", R.drawable.newsimages2, false),
            TopicItemData("Sport", "Sports news and live sports coverage including scores...", R.drawable.newsimages3, false),
            TopicItemData("Travel", "The latest travel news and most significant developm...", R.drawable.newsimages2, false),
            TopicItemData("Money", "The latest breaking financial news on the US and world...", R.drawable.newsimages3, false)
        )
    }

    val authorsState = remember {
        mutableStateListOf(
            AuthorItemData("BBC News", "1.2M Followers", R.drawable.television_icon, Color.Red, true),
            AuthorItemData("CNN", "959K Followers", R.drawable.television_icon, Color.Red, false),
            AuthorItemData("Vox", "452K Followers", R.drawable.television_icon, Color.Yellow, false),
            AuthorItemData("USA Today", "325K Followers", R.drawable.television_icon, Color.Blue, false),
            AuthorItemData("CNBC", "211K Followers", R.drawable.television_icon, Color.Blue, false),
            AuthorItemData("CNET", "18K Followers", R.drawable.television_icon, Color.Red, false),
            AuthorItemData("MSN", "15K Followers", R.drawable.television_icon, Color.Blue, false)
        )
    }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearch(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Search",
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
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    onSearch("")
                                }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = { showFilterSheet = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sliders_icon),
                                    contentDescription = "Filter",
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    singleLine = true
                )

                if (searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        divider = {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                            )
                        },
                        indicator = { } // Hide default indicator to use per-tab animated line
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            val animationProgress by animateFloatAsState(
                                targetValue = if (isSelected) 1f else 0f,
                                animationSpec = tween(durationMillis = 250),
                                label = "LineAnim"
                            )

                            Tab(
                                selected = isSelected,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .drawBehind {
                                                if (animationProgress > 0f) {
                                                    val strokeWidth = 2.dp.toPx()
                                                    val y = size.height + 4.dp.toPx()
                                                    val lineWidth = size.width * animationProgress
                                                    val startX = (size.width - lineWidth) / 2
                                                    drawLine(
                                                        color = BluePrimary,
                                                        start = Offset(startX, y),
                                                        end = Offset(startX + lineWidth, y),
                                                        strokeWidth = strokeWidth,
                                                        cap = StrokeCap.Round
                                                    )
                                                }
                                            },
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                },
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isEmpty()) {
                // Show Recent Search when bar is empty
                RecentSearchesContent(
                    recentSearches = recentSearches,
                    onSearchClick = {
                        searchQuery = it
                        onSearch(it)
                    },
                    onRemoveClick = { recentSearches.remove(it) },
                    onClearAll = { recentSearches.clear() }
                )
            } else {
                // Results Content - Show when typing
                Box(modifier = Modifier.weight(1f)) {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            androidx.compose.material3.CircularProgressIndicator(color = BluePrimary)
                        }
                    } else {
                        when (selectedTab) {
                            0 -> NewsTabContent(
                                articles = searchResults,
                                onNewsItemClick = onNewsItemClick,
                                onActionsClick = { selectedArticleForActions = it }
                            )
                            1 -> TopicsTabContent(
                                topics = topicsState,
                                onToggleSave = { topic ->
                                    val index = topicsState.indexOf(topic)
                                    if (index != -1) {
                                        topicsState[index] = topic.copy(isSaved = !topic.isSaved)
                                    }
                                }
                            )
                            2 -> AuthorTabContent(
                                authors = authorsState,
                                onToggleFollow = { author ->
                                    val index = authorsState.indexOf(author)
                                    if (index != -1) {
                                        val isFollowing = !author.isFollowing
                                        authorsState[index] = author.copy(isFollowing = isFollowing)
                                        
                                        // Update in Firestore
                                        val sourceId = author.name.lowercase().replace(" ", "-")
                                        if (isFollowing) {
                                            onFollowSource(sourceId)
                                        } else {
                                            onUnfollowSource(sourceId)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            onApplyFilters = { filter ->
                showFilterSheet = false
                // Handle filter logic in ViewModel
            },
            onResetFilters = {
                showFilterSheet = false
            }
        )
    }

    selectedArticleForActions?.let { article ->
        NewsActionsBottomSheet(
            onDismissRequest = { selectedArticleForActions = null },
            articleTitle = article.title,
            articleUrl = article.url,
            sourceName = article.sourceName,
            onBookmarkClick = {
                onBookmarkArticle(article)
            },
            onHideClick = {
                onHideArticle(article)
            },
            onBlockSourceClick = {
                onBlockSource(article.sourceId)
            },
            onReportClick = {
                onReportArticle(article)
            }
        )
    }
}

@Composable
fun RecentSearchesContent(
    recentSearches: List<String>,
    onSearchClick: (String) -> Unit,
    onRemoveClick: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Search",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            TextButton(onClick = onClearAll) {
                Text(
                    text = "Clear All",
                    modifier = Modifier.offset(x = 5.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentSearches) { search ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchClick(search) }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.clock_line_icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = search,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { onRemoveClick(search) }, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsTabContent(
    articles: List<com.sharjeel.newsapp.domain.model.Article>,
    onNewsItemClick: (String) -> Unit = {},
    onActionsClick: (com.sharjeel.newsapp.domain.model.Article) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(articles) { article ->
                NewsItem(
                    category = article.sourceName,
                    title = article.title,
                    publisher = article.sourceName,
                    publishedAt = TimeUtils.formatRelativeTime(article.publishedAt),
                    image = R.drawable.newsimages,
                    remoteImageUrl = article.urlToImage,
                    articleUrl = article.url,
                    onItemClick = { onNewsItemClick(article.url) },
                    onActionsClick = { onActionsClick(article) }
                )
        }
    }
}

@Composable
fun TopicsTabContent(
    topics: List<TopicItemData>,
    onToggleSave: (TopicItemData) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(topics) { topic ->
            SearchTopicItem(
                topic = topic,
                onToggleSave = { onToggleSave(topic) }
            )
        }
    }
}

@Composable
fun AuthorTabContent(
    authors: List<AuthorItemData>,
    onToggleFollow: (AuthorItemData) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(authors) { author ->
            SearchAuthorItem(
                author = author,
                onToggleFollow = { onToggleFollow(author) }
            )
        }
    }
}

data class TopicItemData(val name: String, val description: String, val image: Int, val isSaved: Boolean)
data class AuthorItemData(val name: String, val followers: String, val icon: Int, val iconBg: Color, val isFollowing: Boolean)

@Composable
fun SearchTopicItem(
    topic: TopicItemData,
    onToggleSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = topic.image),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = topic.description,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (topic.isSaved) {
            Button(
                onClick = onToggleSave,
                modifier = Modifier.height(34.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Saved",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
            OutlinedButton(
                onClick = onToggleSave,
                modifier = Modifier.height(34.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, BluePrimary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BluePrimary,
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun SearchAuthorItem(
    author: AuthorItemData,
    onToggleFollow: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(author.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = author.icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = author.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = author.followers,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (author.isFollowing) {
            Button(
                onClick = onToggleFollow,
                modifier = Modifier.height(34.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Following",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
            OutlinedButton(
                onClick = onToggleFollow,
                modifier = Modifier.height(34.dp),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, BluePrimary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BluePrimary,
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Follow",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    NewsAppTheme {
        SearchScreenContent(
            searchResults = emptyList(),
            isLoading = false,
            onSearch = {},
            onFollowSource = {},
            onUnfollowSource = {},
            onBookmarkArticle = {},
            onHideArticle = {},
            onBlockSource = {},
            onReportArticle = {},
            onBackClick = {},
            onNewsItemClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenDarkPreview() {
    NewsAppTheme {
        SearchScreenContent(
            searchResults = emptyList(),
            isLoading = false,
            onSearch = {},
            onFollowSource = {},
            onUnfollowSource = {},
            onBookmarkArticle = {},
            onHideArticle = {},
            onBlockSource = {},
            onReportArticle = {},
            onBackClick = {},
            onNewsItemClick = {}
        )
    }
}
