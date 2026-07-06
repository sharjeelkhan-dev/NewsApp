package com.sharjeel.newsapp.ui.screens.author_profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.screens.home.NewsItem
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorProfileScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(1) } // "Recent" selected
    val tabs = listOf("News", "Recent")
    var isFollowing by remember { mutableStateOf(true) }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("") },
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
            // Stats Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // BBC Logo Circle
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBB1919)), // BBC Red
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "BBC",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 14.sp
                        )
                        Text(
                            text = "NEWS",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 14.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AuthorStat(number = "1.2M", label = "Followers")
                    AuthorStat(number = "124K", label = "Following")
                    AuthorStat(number = "326", label = "News")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BBC News",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "is an operational business division of the British Broadcasting Corporation responsible for the gathering and broadcasting of news and current affairs.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 21.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isFollowing) {
                    Button(
                        onClick = { isFollowing = false },
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
                            text = "Following",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = { isFollowing = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, BluePrimary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BluePrimary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Follow",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
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
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            // Robust Tab Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.width(200.dp)) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        divider = {},
                        indicator = {}
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
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    NewsItem(
                        category = "Europe",
                        title = "Ukraine's President Zelenskyy to BBC: Blood money being paid...",
                        publisher = "BBC News",
                        time = "14m ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = { /* Already on profile */ },
                        onItemClick = { /* Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Sport",
                        title = "Frankfurt stun Barcelona to reach semi-finals",
                        publisher = "BBC News",
                        time = "1h ago",
                        image = R.drawable.newsimages3,
                        onAuthorClick = { /* Already on profile */ },
                        onItemClick = { /* Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Europe",
                        title = "Russian warship: Moskva sinks in Black Sea",
                        publisher = "BBC News",
                        time = "4h ago",
                        image = R.drawable.newsimages,
                        onAuthorClick = { /* Already on profile */ },
                        onItemClick = { /* Detail */ }
                    )
                }
                item {
                    NewsItem(
                        category = "Politic",
                        title = "Madhya Pradesh: Why an Indian state is demolishing Muslim",
                        publisher = "BBC News",
                        time = "4h ago",
                        image = R.drawable.newsimages2,
                        onAuthorClick = { /* Already on profile */ },
                        onItemClick = { /* Detail */ }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthorStat(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorProfileScreenPreview() {
    NewsAppTheme {
        AuthorProfileScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AuthorProfileScreenDarkPreview() {
    NewsAppTheme {
        AuthorProfileScreen(onBackClick = {})
    }
}
