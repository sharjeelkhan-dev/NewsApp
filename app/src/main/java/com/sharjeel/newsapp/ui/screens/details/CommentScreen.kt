package com.sharjeel.newsapp.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

data class Comment(
    val id: Int,
    val author: String,
    val text: String,
    val time: String,
    val likesCount: Int,
    val avatar: Int,
    val replies: Int = 0,
    val isLiked: Boolean = false
)

@Composable
fun CommentScreen(
    onBackClick: () -> Unit
) {
    val comments = remember {
        mutableStateListOf(
            Comment(1, "Wilson Franci", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "4w", 125, R.drawable.ic_avatar),
            Comment(2, "Madelyn Saris", "Lorem Ipsum is simply dummy text of the printing and type...", "4w", 3, R.drawable.ic_avatar, 2, isLiked = true),
            Comment(3, "Marley Botosh", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "4w", 12, R.drawable.ic_avatar, 2),
            Comment(4, "Alfonso Septimus", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "4w", 14000, R.drawable.ic_avatar, 58, isLiked = true),
            Comment(5, "Omar Herwitz", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "4w", 16, R.drawable.ic_avatar),
            Comment(6, "Corey Geidt", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.", "4w", 125, R.drawable.ic_avatar)
        )
    }

    var commentText by remember { mutableStateOf("") }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            // 1. Header Row (Fixed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
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
                Text(
                    text = "Comments",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                // Empty spacer for balance
                Spacer(modifier = Modifier.width(24.dp))
            }

            // 2. Comments List (Scrollable Middle)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
            ) {
                items(comments, key = { it.id }) { comment ->
                    CommentItem(
                        comment = comment,
                        onLikeToggle = {
                            val index = comments.indexOfFirst { it.id == comment.id }
                            if (index != -1) {
                                val currentComment = comments[index]
                                val newIsLiked = !currentComment.isLiked
                                val newLikesCount = if (newIsLiked) currentComment.likesCount + 1 else currentComment.likesCount - 1
                                comments[index] = currentComment.copy(isLiked = newIsLiked, likesCount = newLikesCount)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 3. Bottom Input Section (Fixed Footer)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .navigationBarsPadding()
                    .imePadding() // Keyboard handling
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { 
                            Text("Type your comment", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), fontSize = 14.sp) 
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { /* Send comment */ },
                        modifier = Modifier.size(54.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.vector),
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onLikeToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = comment.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = comment.author,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.time,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Likes Button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .clickable { onLikeToggle() }
                            .padding(2.dp),
                        tint = if (comment.isLiked) Color(0xFFED2E7E) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatLikes(comment.likesCount),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (comment.isLiked) Color(0xFFED2E7E) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Reply Button
                Row(
                    modifier = Modifier.clickable { /* Handle reply */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.reply_arrow_outline_icon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "reply",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
            if (comment.replies > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "See more (${comment.replies})",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.clickable { /* Show replies */ }
                )
            }
        }
    }
}

private fun formatLikes(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}K likes"
        else -> "$count likes"
    }
}

@Preview(showBackground = true)
@Composable
fun CommentScreenPreview() {
    NewsAppTheme {
        CommentScreen(onBackClick = {})
    }
}