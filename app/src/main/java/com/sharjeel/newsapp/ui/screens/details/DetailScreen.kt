package com.sharjeel.newsapp.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    var isFollowing by remember { mutableStateOf(true) }
    var isLiked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(true) }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            // 1. Top Action Icons Row (Fixed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /* Share action */ },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share_line_icon),
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(
                    onClick = { /* More action */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // 2. Scrollable Middle Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {

                // Author Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFBB1919)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "BBC\nNEWS",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BBC News",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "14m ago",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        )
                    }

                    if (isFollowing) {
                        Button(
                            onClick = { isFollowing = false },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BluePrimary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                        ) {
                            Text(
                                "Following",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = { isFollowing = true },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BluePrimary),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = BluePrimary
                            ),
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                        ) {
                            Text(
                                "Follow",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // News Image
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    painterResource(id = R.drawable.newsimages).let { painter ->
                        androidx.compose.foundation.Image(
                            painter = painter,
                            contentDescription = "Article Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content Category & Paragraphs
                Text(
                    text = "Europe",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ukraine's President Zelensky to BBC: Blood money being paid for Russian oil",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        lineHeight = 30.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ukrainian President Volodymyr Zelensky has accused European countries that continue to buy Russian oil of \"earning their money in other people's blood\".",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = $$"In an interview with the BBC, President Zelensky singled out Germany and Hungary, accusing them of blocking efforts to embargo energy sales, from which Russia stands to make up to £250bn ($326bn) this year.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))
            }

            // 3. Bottom Interaction Row (Fixed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Likes Button
                Row(
                    modifier = Modifier.clickable { isLiked = !isLiked },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "Likes",
                        tint = if (isLiked) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isLiked) "24.6K" else "24.5K",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                // Comments Button
                Row(
                    modifier = Modifier.clickable { onCommentClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                        Icon(
                            painter = painterResource(id = R.drawable.talk_bubble_icon),
                        contentDescription = "Comments",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "1K",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bookmark Button
                IconButton(
                    onClick = { isBookmarked = !isBookmarked },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.saved_bookmark_icon),
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) BluePrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NewsAppTheme {
        DetailScreen(onBackClick = {}, onCommentClick = {})
    }

}
