package com.sharjeel.newsapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.sharjeel.newsapp.util.SmoothScrollConfig
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AkhbarLogo
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NewsAppTheme {
        HomeScreen(
            onSeeAllTrendingClick = {},
            onNotificationClick = {},
            onSeeAllLatestClick = {},
            onSearchClick = {},
            onAuthorClick = {},
            onFilterClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeeAllTrendingClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSeeAllLatestClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onFilterClick: () -> Unit = {}
) {
    val categories = listOf("All", "Sports", "Politics", "Business", "Health", "Travel", "Science")
    var selectedCategory by remember { mutableStateOf("All") }
    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()

    AppScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header (Locked)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AkhbarLogo(
                    iconSize = 30.dp,
                    fontSize = 24.sp
                )
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
            // Search Bar (Locked) - Clickable trigger
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSearchClick
                    ),
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
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                enabled = false,
                readOnly = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Trending Section (Locked)
            TrendingSection(
                onSeeAllClick = onSeeAllTrendingClick,
                onAuthorClick = onAuthorClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Latest Section Header (Locked)
            LatestSectionHeader(onSeeAllClick = onSeeAllLatestClick)

            Spacer(modifier = Modifier.height(16.dp))

            // Category Tabs (Locked)
            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Content Area
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                flingBehavior = flingBehavior,
                verticalArrangement = Arrangement.spacedBy(22.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item(key = "news_1") {
                    NewsItem(
                        category = "Europe",
                        title = "Ukraine's President Zelenskyy to BBC: Blood money being paid for Russian oil",
                        publisher = "BBC News",
                        time = "14m ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Detail */ }
                    )
                }
                item(key = "news_2") {
                    NewsItem(
                        category = "Travel",
                        title = "Her train broke down. Her phone died. And then she met her future husband",
                        publisher = "CNN",
                        time = "1h ago",
                        image = R.drawable.newsimages3,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Detail */ }
                    )
                }
                item(key = "news_3") {
                    NewsItem(
                        category = "Business",
                        title = "Global markets brace for impact as interest rates rise again",
                        publisher = "Reuters",
                        time = "3h ago",
                        image = R.drawable.newsimages,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Detail */ }
                    )
                }
                item(key = "news_4") {
                    NewsItem(
                        category = "Science",
                        title = "New discovery in deep space challenges our understanding of the universe",
                        publisher = "NASA",
                        time = "5h ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = onAuthorClick,
                        onItemClick = { /* Detail */ }
                    )
                }
            }
        }
    }
}

@Composable
fun TrendingSection(
    onSeeAllClick: () -> Unit,
    onAuthorClick: () -> Unit = {}
) {
    Column {
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
        Image(
            painter = painterResource(id = R.drawable.newsimages),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(183.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Europe",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Russian warship: Moskva sinks in Black Sea",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold, 
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Publisher Area - Explicitly clickable with Ripple
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onAuthorClick)
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .offset(x = (-4).dp)
            ) {
                // High-fidelity BBC Style Logo
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBB1919)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "B", 
                        color = Color.White, 
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BBC News",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.clock_line_icon),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "4h ago",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { /* TODO */ },
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

@Composable
fun LatestSectionHeader(
    onSeeAllClick: () -> Unit
) {
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
        flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()
    ) {
        items(
            items = categories,
            key = { it }
        ) { category ->
            val isSelected = category == selectedCategory
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = category,
                    modifier = Modifier.clickable { onCategorySelected(category) },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                if (isSelected) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(2.dp)
                            .background(BluePrimary)
                    )
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    category: String,
    title: String,
    publisher: String,
    time: String,
    image: Int,
    publisherIcon: Int? = null, // Added parameter
    onAuthorClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onItemClick
            ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = category,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy
                    (fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Publisher Area - Explicitly clickable with Ripple
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onAuthorClick)
                        .padding(vertical = 2.dp, horizontal = 4.dp)
                        .offset(x = (-4).dp)
                ) {
                    if (publisherIcon != null) {
                         Image(
                            painter = painterResource(id = publisherIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // High-fidelity BBC Style Logo
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFBB1919)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "B", 
                                color = Color.White, 
                                fontSize = 10.sp, 
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = publisher,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /* TODO */ },
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
