package com.sharjeel.newsapp.ui.screens.bookmark

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig

@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview() {
    NewsAppTheme {
        BookmarkScreen()
    }
}

@Composable
fun BookmarkScreen(
    onNewsItemClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
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
                    IconButton(onClick = { /* TODO: Implement filter */ }) {
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
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bookmarked News List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                flingBehavior = flingBehavior,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(5, key = { it }) { index ->
                    NewsItem(
                        category = "Europe",
                        title = "Ukraine's President Zelenskyy to BBC: Blood money being paid for Russian oil",
                        publisher = "BBC News",
                        time = "14m ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = { /* Bookmark screens navigation usually goes to detail or author */ },
                        onItemClick = onNewsItemClick
                    )
                }
            }
        }
    }
}
