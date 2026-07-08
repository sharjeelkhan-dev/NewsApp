package com.sharjeel.newsapp.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AkhbarLogo
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig
import com.sharjeel.newsapp.util.TimeUtils

// ================= PREVIEW =================
@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
fun HomeScreenPreview() {
    NewsAppTheme {
        val mockTrendingList = listOf(
            com.sharjeel.newsapp.domain.model.Article(
                title = "Russian warship fires warning shots on cargo ship in Black Sea",
                sourceName = "BBC News",
                sourceId = "bbc-news",
                url = "https://www.bbc.com/news/world-europe",
                urlToImage = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=500&auto=format&fit=crop&q=60",
                publishedAt = "2026-07-08T12:00:00Z",
                author = "BBC Reporter",
                description = "A Russian warship has fired warning shots on a cargo ship moving towards Ukraine.",
                content = "Full content body of the news goes here for mock validation purposes."
            )
        )
        val mockLatestList = listOf(
            com.sharjeel.newsapp.domain.model.Article(
                title = "Ukraine updates: Kyiv says grain facilities hit again in Odesa",
                sourceName = "CNN",
                sourceId = "cnn",
                url = "https://www.cnn.com",
                urlToImage = "https://images.unsplash.com/photo-1495020689067-958852a6565d?w=500&auto=format&fit=crop&q=60",
                publishedAt = "2026-07-08T11:30:00Z",
                author = "CNN Staff",
                description = "Kyiv reports continuous infrastructure damage across vital deep-sea ports.",
                content = "Full detailed analytics response content body from regional correspondents."
            )
        )

        HomeScreenContent(
            trendingNews = mockTrendingList,
            latestNews = mockLatestList,
            isLoading = false,
            selectedCategory = "All",
            onCategorySelected = {},
            onSeeAllTrendingClick = {},
            onNotificationClick = {},
            onSeeAllLatestClick = {},
            onSearchClick = {},
            onAuthorClick = {},
            onFilterClick = {},
            onNewsItemClick = {}
        )
    }
}

@Composable
fun HomeScreen(
    onSeeAllTrendingClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSeeAllLatestClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onFilterClick: () -> Unit = {},
    onNewsItemClick: (String) -> Unit = { _ -> },
    viewModel: HomeViewModel = hiltViewModel()
) {
    val trendingNews by viewModel.trendingNews
    val latestNews by viewModel.latestNews
    val isLoading by viewModel.isLoading
    var selectedCategory by remember { mutableStateOf("All") }

    HomeScreenContent(
        trendingNews = trendingNews,
        latestNews = latestNews,
        isLoading = isLoading,
        selectedCategory = selectedCategory,
        onCategorySelected = { category ->
            selectedCategory = category
            viewModel.loadLatestNews(category)
        },
        onSeeAllTrendingClick = onSeeAllTrendingClick,
        onNotificationClick = onNotificationClick,
        onSeeAllLatestClick = onSeeAllLatestClick,
        onSearchClick = onSearchClick,
        onAuthorClick = onAuthorClick,
        onFilterClick = onFilterClick,
        onNewsItemClick = onNewsItemClick
    )
}

@Composable
fun HomeScreenContent(
    trendingNews: List<com.sharjeel.newsapp.domain.model.Article>,
    latestNews: List<com.sharjeel.newsapp.domain.model.Article>,
    isLoading: Boolean,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSeeAllTrendingClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSeeAllLatestClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNewsItemClick: (String) -> Unit
) {
    val categories = listOf("All", "Sports", "Politics", "Business", "Health", "Travel", "Science")
    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()

    val displayLatestList by remember(trendingNews, latestNews) {
        derivedStateOf {
            if (trendingNews.isEmpty() && latestNews.size > 1) latestNews.drop(1) else latestNews
        }
    }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        // Main structural wrapper
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ================= FIXED / STICKY TOP HEADER & SEARCH =================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AkhbarLogo(iconSize = 30.dp, fontSize = 24.sp)
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.bell_line_icon),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = androidx.compose.material3.ripple(),
                            onClick = onSearchClick
                        ),
                    placeholder = {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
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
                        IconButton(onClick = onFilterClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.sliders_icon),
                                contentDescription = "Filter",
                                modifier = Modifier.size(22.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    enabled = false,
                    readOnly = true
                )
            }

            // ================= SCROLLABLE CONTENT SECTION =================
            if (isLoading && trendingNews.isEmpty() && latestNews.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BluePrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    flingBehavior = flingBehavior,
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    if (trendingNews.isNotEmpty()) {
                        item(key = "trending_section") {
                            val trendingItem = trendingNews.first()
                            TrendingSection(
                                title = trendingItem.title,
                                category = trendingItem.sourceName,
                                imageUrl = trendingItem.urlToImage,
                                publisher = trendingItem.sourceName,
                                publishedAt = trendingItem.publishedAt,
                                articleUrl = trendingItem.url,
                                onSeeAllClick = onSeeAllTrendingClick,
                                onAuthorClick = { onAuthorClick(trendingItem.sourceId) },
                                onItemClick = { onNewsItemClick(trendingItem.url) }
                            )
                        }
                    } else if (latestNews.isNotEmpty()) {
                        item(key = "trending_fallback") {
                            val fallbackItem = latestNews.first()
                            TrendingSection(
                                title = fallbackItem.title,
                                category = fallbackItem.sourceName,
                                imageUrl = fallbackItem.urlToImage,
                                publisher = fallbackItem.sourceName,
                                publishedAt = fallbackItem.publishedAt,
                                articleUrl = fallbackItem.url,
                                onSeeAllClick = onSeeAllTrendingClick,
                                onAuthorClick = { onAuthorClick(fallbackItem.sourceId) },
                                onItemClick = { onNewsItemClick(fallbackItem.url) }
                            )
                        }
                    }

                    item(key = "latest_and_categories") {
                        Column {
                            LatestSectionHeader(onSeeAllClick = onSeeAllLatestClick)
                            Spacer(modifier = Modifier.height(16.dp))
                            CategoryTabs(
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = onCategorySelected
                            )
                        }
                    }

                    items(
                        items = displayLatestList,
                        key = { it.url }
                    ) { article ->
                        NewsItem(
                            category = article.sourceName,
                            title = article.title,
                            publisher = article.sourceName,
                            publishedAt = article.publishedAt,
                            image = R.drawable.newsimages,
                            remoteImageUrl = article.urlToImage,
                            articleUrl = article.url,
                            onAuthorClick = { onAuthorClick(article.sourceId) },
                            onItemClick = { onNewsItemClick(article.url) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingSection(
    title: String,
    category: String,
    imageUrl: String?,
    publisher: String,
    publishedAt: String,
    articleUrl: String,
    onSeeAllClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val logoDomain = remember(articleUrl) { if (!articleUrl.isNullOrBlank()) TimeUtils.getDomain(articleUrl) else null }
    val logoUrl = remember(logoDomain) { if (!logoDomain.isNullOrBlank()) "https://www.google.com/s2/favicons?sz=128&domain=$logoDomain" else "" }
    val cleanedPublisher = remember(publisher) { sanitizeChannelName(publisher) }
    val time = remember(publishedAt) { TimeUtils.formatRelativeTime(publishedAt) }
    val upperCategory = remember(logoDomain, category) {
        if (!logoDomain.isNullOrBlank()) logoDomain.replace("www.", "").split(".")[0].uppercase() else category.uppercase()
    }

    Column(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onItemClick
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trending",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = "See all",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (!imageUrl.isNullOrBlank()) imageUrl else R.drawable.newsimages)
                .crossfade(100)
                .placeholder(R.drawable.newsimages)
                .error(R.drawable.newsimages)
                .fallback(R.drawable.newsimages)
                .memoryCacheKey(imageUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(183.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = upperCategory,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onAuthorClick)
            ) {
                if (!logoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(logoUrl)
                            .crossfade(true)
                            .memoryCacheKey(logoUrl)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(BluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cleanedPublisher.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = cleanedPublisher,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock_line_icon),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { /* Menu */ },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun LatestSectionHeader(onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Latest",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(
            text = "See all",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(end = 24.dp),
        flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()
    ) {
        items(items = categories, key = { it }) { category ->
            val isSelected = category == selectedCategory
            val animationProgress by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0f,
                animationSpec = tween(durationMillis = 250),
                label = "LineAnim"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = category,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onCategorySelected(category) }
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
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun NewsItem(
    category: String,
    title: String,
    publisher: String,
    publishedAt: String,
    image: Int,
    remoteImageUrl: String?,
    articleUrl: String,
    onAuthorClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val logoDomain = remember(articleUrl) { if (!articleUrl.isNullOrBlank()) TimeUtils.getDomain(articleUrl) else null }
    val logoUrl = remember(logoDomain) { if (!logoDomain.isNullOrBlank()) "https://www.google.com/s2/favicons?sz=128&domain=$logoDomain" else "" }
    val cleanedPublisher = remember(publisher) { sanitizeChannelName(publisher) }
    val time = remember(publishedAt) { TimeUtils.formatRelativeTime(publishedAt) }
    val upperCategory = remember(logoDomain, category) {
        if (!logoDomain.isNullOrBlank()) logoDomain.replace("www.", "").split(".")[0].uppercase() else category.uppercase()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = androidx.compose.material3.ripple(),
                onClick = onItemClick
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (!remoteImageUrl.isNullOrBlank()) remoteImageUrl else image)
                .crossfade(100)
                .placeholder(R.drawable.newsimages)
                .error(R.drawable.newsimages)
                .fallback(R.drawable.newsimages)
                .memoryCacheKey(remoteImageUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .height(96.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = upperCategory,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onAuthorClick)
                ) {
                    if (!logoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(logoUrl)
                                .crossfade(true)
                                .memoryCacheKey(logoUrl)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(BluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cleanedPublisher.take(1).uppercase(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = cleanedPublisher,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock_line_icon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = { /* Actions */ },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun sanitizeChannelName(name: String): String {
    if (name.isBlank()) return "News"
    return name.split(",")[0]
        .replace(Regex("(?i)https?://(www\\.)?"), "")
        .split(".")[0]
        .trim()
}