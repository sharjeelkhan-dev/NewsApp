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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig

// 1. DATA MODELS
data class ExploreTopicData(val name: String, val description: String, val image: Int, val isSaved: Boolean)
data class ExplorePopularData(val category: String, val title: String, val publisher: String, val time: String, val image: Int)

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
fun ExploreScreenPreview() {
    NewsAppTheme {
        ExploreScreen(onSearchClick = {}, onAuthorClick = {})
    }
}

@Composable
fun ExploreScreen(
    onSearchClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onNewsItemClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val topics = remember {
        mutableStateListOf(
            ExploreTopicData("Health", "Get energizing workout moves, healthy recipes and explore articles on...", R.drawable.newsimages, false),
            ExploreTopicData("Technology", "the application of scientific knowledge to the practi...", R.drawable.newsimages2, true),
            ExploreTopicData("Art", "Art is a diverse range of human activity, and result...", R.drawable.newsimages3, true)
        )
    }

    val popularArticles = remember {
        listOf(
            ExplorePopularData("Europe", "Ukraine's President Zelenskyy to BBC: Blood money being paid for R...", "BBC News", "14m ago", R.drawable.newsimages2),
            ExplorePopularData("Europe", "Russian warship: Moskva sinks in Black Sea", "BBC News", "4h ago", R.drawable.newsimages),
            ExplorePopularData("Europe", "Ukraine's President Zelenskyy to BBC: Blood money being paid for R...", "BBC News", "14m ago", R.drawable.newsimages2),
            ExplorePopularData("Europe", "Russian warship: Moskva sinks in Black Sea", "BBC News", "4h ago", R.drawable.newsimages),
            ExplorePopularData("Europe", "Ukraine's President Zelenskyy to BBC: Blood money being paid for R...", "BBC News", "14m ago", R.drawable.newsimages2)
        )
    }

    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()


    AppScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ){

            // ================= FIXED HEADER AREA =================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                Spacer(modifier = Modifier.height(24.dp))
            }

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
                        modifier = Modifier
                            .fillMaxWidth(),
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
                    Box {
                        ExploreTopicItem(
                            topic = topic,
                            onToggleSave = {
                                topics[index] = topic.copy(isSaved = !topic.isSaved)
                            }
                        )
                    }
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

                // Fixed Bookmark-Style Row Layout implementation loop
                itemsIndexed(items = popularArticles, key = { idx, item -> "pop_${item.title}_$idx" }) { _, article ->
                    Box {
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

// ================= COMPONENT DESIGNS =================

@Composable
fun ExploreTopicItem(topic: ExploreTopicData, onToggleSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(topic.image)
                .crossfade(true)
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
                    .width(78.dp)
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
                    .width(78.dp)
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
    article: ExplorePopularData,
    onAuthorClick: () -> Unit,
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        // Main Image Area - Full Width as seen in the image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(183.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category Label
        Text(
            text = article.category,
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

        // Sub-row for publisher info and more action
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular identifier avatar
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFBB1919))
                    .clickable { onAuthorClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("B", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = article.publisher,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onAuthorClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                painter = painterResource(id = R.drawable.clock_line_icon),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = article.time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /* Action popup context options */ }, modifier = Modifier.size(24.dp)) {
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