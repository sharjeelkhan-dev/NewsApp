package com.sharjeel.newsapp.ui.screens.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNewsItemClick: () -> Unit = {},
    onCreateNewsClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(1) } // "Recent" default selected
    val tabs = listOf("News", "Recent")
    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()

    // FloatingActionButton configuration intact
    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewsClick,
                containerColor = BluePrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus_line_icon), // vector configuration matched
                    contentDescription = "Add News",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { padding ->
        // EXACT ARCHITECTURE: Single Parent Column with global horizontal padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // 1. TOP HEADER GROUP (Ab yeh TopAppBar ke bina direct parent column ke andar hai)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. PROFILE DETAILS FRAMES
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "User Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStat(number = "2156", label = "Followers")
                        ProfileStat(number = "567", label = "Following")
                        ProfileStat(number = "23", label = "News")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Wilson Franci",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 21.sp,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onEditProfileClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Edit profile",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Website",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            // 3. TAB AREA SELECTOR
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    divider = {},
                    indicator = {},
                    modifier = Modifier.width(220.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            text = {
                                val animationProgress by animateFloatAsState(
                                    targetValue = if (isSelected) 1f else 0f,
                                    animationSpec = tween(durationMillis = 250),
                                    label = "TabLineAnimation"
                                )
                                val indicatorColor = MaterialTheme.colorScheme.primary
                                Text(
                                    text = title,
                                    modifier = Modifier
                                        .drawBehind {
                                            if (animationProgress > 0f) {
                                                val strokeWidth = 2.dp.toPx()
                                                val y = size.height + 6.dp.toPx()

                                                val lineWidth = size.width * animationProgress
                                                val startX = (size.width - lineWidth) / 2

                                                drawLine(
                                                    color = indicatorColor,
                                                    start = Offset(startX, y),
                                                    end = Offset(startX + lineWidth, y),
                                                    strokeWidth = strokeWidth,
                                                    cap = StrokeCap.Round
                                                )
                                            }
                                        },
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    maxLines = 1
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.onBackground,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. SCROLLABLE FEED CONTAINER AREA
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                state = listState,
                flingBehavior = flingBehavior,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(4, key = { it }) { index ->
                    val sampleCategories = listOf("NFTs", "Business", "Travel", "Health")
                    val sampleTitles = listOf(
                        "Minting Your First NFT: A Beginner's Guide to Creating...",
                        "5 things to know before the stock market opens Monday",
                        "Bali plans to reopen to international tourists in Septe...",
                        "Healthy Living: Diet and Exercise"
                    )
                    val sampleImages = listOf(R.drawable.newsimages, R.drawable.newsimages2, R.drawable.newsimages3, R.drawable.newsimages)

                    NewsItem(
                        category = sampleCategories[index],
                        title = sampleTitles[index],
                        publisher = "Wilson Franci",
                        time = if(index == 0) "15m ago" else if(index == 1) "1h ago" else if(index == 2) "1w ago" else "2w ago",
                        image = sampleImages[index],
                        publisherIcon = R.drawable.ic_avatar,
                        onAuthorClick = { },
                        onItemClick = onNewsItemClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStat(number: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NewsAppTheme {
        ProfileScreen(
            onSettingsClick = {},
            onEditProfileClick = {}
        )
    }
}