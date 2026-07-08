package com.sharjeel.newsapp.ui.screens.trending

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    onBackClick: () -> Unit,
    onAuthorClick: (String) -> Unit = {},
    onNewsItemClick: (String) -> Unit = {},
    viewModel: TrendingViewModel = hiltViewModel()
) {
    val trendingNews by viewModel.trendingNews
    val isLoading by viewModel.isLoading

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Trending",
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
                    IconButton(onClick = { viewModel.loadTrendingNews() }) {
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
        if (isLoading && trendingNews.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BluePrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                items(trendingNews) { article ->
                    TrendingItem(
                        category = article.sourceName,
                        title = article.title,
                        publisher = article.sourceName,
                        time = TimeUtils.formatRelativeTime(article.publishedAt),
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

@Composable
fun TrendingItem(
    category: String,
    title: String,
    publisher: String,
    time: String,
    image: Int,
    remoteImageUrl: String? = "",
    articleUrl: String = "",
    onAuthorClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    // Google Favicon service use kar rahe hain Home Screen ke sath exact sync ke liye
    val logoDomain = remember(articleUrl) { if (!articleUrl.isNullOrBlank()) TimeUtils.getDomain(articleUrl) else null }
    val logoUrl = if (!logoDomain.isNullOrBlank()) "https://www.google.com/s2/favicons?sz=128&domain=$logoDomain" else ""
    val cleanedPublisher = remember(publisher) { sanitizeChannelName(publisher) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onItemClick
            )
    ) {
        // IMAGE RESILIENCE FIX: Fallbacks ke sath broken URLs ko completely safe kar diya h
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (!remoteImageUrl.isNullOrBlank()) remoteImageUrl else image)
                .crossfade(true)
                .placeholder(R.drawable.newsimages)
                .error(R.drawable.newsimages)
                .fallback(R.drawable.newsimages)
                .build(),
            contentDescription = "Trending News Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(183.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (!logoDomain.isNullOrBlank()) logoDomain.replace("www.", "").split(".")[0].uppercase() else category.uppercase(),
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
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // META META ROW ROW: Fixed Responsive Layout Constraints
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left Side Block: Publisher Title & Logo Anchor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f) // Bounded priority constraints
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onAuthorClick)
            ) {
                if (!logoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(logoUrl)
                            .crossfade(true)
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

            // Right Side Block: Dynamic Timestamp Alignment Anchor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(end = 4.dp)
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
                    onClick = { /* Actions */ },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More Options",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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

@Preview(showBackground = true)
@Composable
fun TrendingScreenPreview() {
    NewsAppTheme {
        TrendingScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TrendingScreenDarkPreview() {
    NewsAppTheme {
        TrendingScreen(onBackClick = {})
    }
}