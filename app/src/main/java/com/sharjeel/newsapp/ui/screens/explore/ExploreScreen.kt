package com.sharjeel.newsapp.ui.screens.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig
import com.sharjeel.newsapp.util.TimeUtils
import java.net.URI

// 1. DATA MODELS (FIX: Image type updated to String for dynamic URLs)
data class ExploreTopicData(val name: String, val description: String, val imageUrl: String, val isSaved: Boolean)

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
fun ExploreScreenPreview() {
    NewsAppTheme {
        ExploreScreen(onSearchClick = {}, onAuthorClick = { _ -> })
    }
}

@Composable
fun ExploreScreen(
    onSearchClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onNewsItemClick: (String) -> Unit = { _ -> },
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val popularNews by viewModel.popularNews
    val isLoading by viewModel.isLoading

    // FIX: Professional Unsplash Mockup Images perfectly mapped for each topic category
    val topics = remember {
        androidx.compose.runtime.mutableStateListOf(
            ExploreTopicData(
                name = "Health",
                description = "Get energizing workout moves, healthy recipes...",
                imageUrl = "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=150&auto=format&fit=crop&q=60",
                isSaved = false
            ),
            ExploreTopicData(
                name = "Technology",
                description = "the application of scientific knowledge to the practi...",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=150&auto=format&fit=crop&q=60",
                isSaved = true
            ),
            ExploreTopicData(
                name = "Art",
                description = "Art is a diverse range of human activity, and result...",
                imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=150&auto=format&fit=crop&q=60",
                isSaved = true
            )
        )
    }

    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {

            // ================= FIXED HEADER AREA =================
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoading && popularNews.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = BluePrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    flingBehavior = flingBehavior,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // Topic Header Section
                    item(key = "topic_header") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Topic",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )
                            Text(
                                text = "See all",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.clickable { /* Action handling */ }
                            )
                        }
                    }

                    itemsIndexed(items = topics, key = { _, topic -> "topic_${topic.name}" }) { index, topic ->
                        TopicUiItem(
                            topic = topic,
                            onToggleSave = {
                                topics[index] = topic.copy(isSaved = !topic.isSaved)
                            }
                        )
                    }

                    // Section Label for Popular Content
                    item(key = "popular_topic_title") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Popular Topic",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                        )
                    }

                    if (popularNews.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "No popular news found.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { viewModel.loadPopularNews() }) {
                                        Text("Refresh")
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(items = popularNews, key = { idx, item -> "pop_${item.url}_$idx" }) { _, article ->
                            ExploreBookmarkStyleItem(
                                article = article,
                                onAuthorClick = onAuthorClick,
                                onItemClick = onNewsItemClick
                            )
                        }
                    }
                }
            }
        }
    }
}

// ================= COMPONENT DESIGNS =================

@Composable
fun TopicUiItem(topic: ExploreTopicData, onToggleSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FIX: Renders the beautiful Unsplash mockup URLs using Coil asynchronously
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(topic.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.newsimages) // fallback indicator
                .error(R.drawable.newsimages)
                .build(),
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
                    fontSize = 16.sp
                )
            )
            Text(
                text = topic.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        if (topic.isSaved) {
            Button(
                onClick = onToggleSave,
                modifier = Modifier
                    .width(88.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text(
                    "Saved",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        } else {
            OutlinedButton(
                onClick = onToggleSave,
                modifier = Modifier
                    .width(88.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 0.dp),
                border = BorderStroke(1.dp, BluePrimary)
            ) {
                Text(
                    "Save",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )
                )
            }
        }
    }
}

@Composable
fun ExploreBookmarkStyleItem(
    article: com.sharjeel.newsapp.domain.model.Article,
    onAuthorClick: (String) -> Unit,
    onItemClick: (String) -> Unit = { _ -> }
) {
    val logoUrl = remember(article.url) {
        try {
            val uri = URI(article.url)
            val domain = uri.host?.let { host ->
                if (host.startsWith("www.")) host.substring(4) else host
            }
            if (!domain.isNullOrEmpty()) {
                "https://www.google.com/s2/favicons?sz=64&domain=$domain"
            } else ""
        } catch (e: Exception) {
            ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(article.url) }
    ) {
        // Main Image Area
        if (article.urlToImage.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .placeholder(R.drawable.newsimages)
                    .error(R.drawable.newsimages)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(183.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(183.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category / Source Label
        Text(
            text = article.sourceName,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Article Title
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Structural flow adjustment to prevent vertical overlapping layout crashes
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onAuthorClick(article.sourceId) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (logoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(logoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        contentScale = ContentScale.Fit,
                        error = painterResource(id = R.drawable.ic_launcher_foreground)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(BluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = article.sourceName.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = article.sourceName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.clock_line_icon),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = TimeUtils.formatRelativeTime(article.publishedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { /* Action popup context options */ },
                    modifier = Modifier.size(24.dp)
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