package com.sharjeel.newsapp.ui.screens.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.SmoothScrollConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClick: () -> Unit
) {
    var allRead by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val flingBehavior = SmoothScrollConfig.rememberSmoothFlingBehavior()
    
    AppScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Notification",
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
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        "Mark all as read",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) 
                                },
                                onClick = {
                                    allRead = true
                                    showMenu = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            state = listState,
            flingBehavior = flingBehavior,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item(key = "header_today") {
                NotificationHeader("Today, April 22")
            }
            item(key = "notif_1") {
                NotificationItem(
                    avatar = R.drawable.television_icon,
                    avatarTint = Color.Red,
                    isLogo = true,
                    isRead = allRead,
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("BBC News")
                        }
                        append(" has posted new europe news \"Ukraine's President Zele...\"")
                    },
                    time = "15m ago"
                )
            }
            item(key = "notif_2") {
                NotificationItem(
                    avatar = R.drawable.ic_avatar,
                    avatarTint = BluePrimary,
                    isRead = allRead,
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("Modelyn Saris")
                        }
                        append(" is now following you")
                    },
                    time = "1h ago",
                    action = {
                        var isFollowing by remember { mutableStateOf(false) }
                        FollowButton(
                            isFollowing = isFollowing,
                            onToggle = { isFollowing = !isFollowing }
                        )
                    }
                )
            }
            item(key = "notif_3") {
                NotificationItem(
                    avatar = R.drawable.ic_avatar,
                    avatarTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    isRead = true, // Always read for variety in UI
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("Omar Merditz")
                        }
                        append(" comment to your news \"Minting Your First NFT: A... \"")
                    },
                    time = "1h ago"
                )
            }

            item(key = "header_yesterday") {
                NotificationHeader("Yesterday, April 21")
            }
            item(key = "notif_4") {
                NotificationItem(
                    avatar = R.drawable.ic_avatar,
                    avatarTint = Color.Green,
                    isRead = true,
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("Marley Botosh")
                        }
                        append(" is now following you")
                    },
                    time = "1 Day ago",
                    action = {
                        var isFollowing by remember { mutableStateOf(false) }
                        FollowButton(
                            isFollowing = isFollowing,
                            onToggle = { isFollowing = !isFollowing }
                        )
                    }
                )
            }
            item(key = "notif_5") {
                NotificationItem(
                    avatar = R.drawable.ic_avatar,
                    avatarTint = Color.Magenta,
                    isRead = true,
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("Modelyn Saris")
                        }
                        append(" likes your news \"Minting Your First NFT: A... \"")
                    },
                    time = "1 Day ago"
                )
            }
            item(key = "notif_6") {
                NotificationItem(
                    avatar = R.drawable.television_icon,
                    avatarTint = Color.Red,
                    isLogo = true,
                    isRead = true,
                    content = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)) {
                            append("CNN")
                        }
                        append(" has posted new travel news \"Her train broke down. Her pho...\"")
                    },
                    time = "1 Day ago"
                )
            }
        }
    }
}

@Composable
fun NotificationHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold, 
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun NotificationItem(
    avatar: Int,
    avatarTint: Color,
    isLogo: Boolean = false,
    isRead: Boolean = false,
    content: AnnotatedString,
    time: String,
    action: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        color = if (isRead) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (isLogo) avatarTint else avatarTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = avatar),
                    contentDescription = null,
                    modifier = Modifier.size(if (isLogo) 30.dp else 42.dp),
                    tint = if (isLogo) Color.White else avatarTint
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                )
            }

            if (action != null) {
                Spacer(modifier = Modifier.width(8.dp))
                action()
            }
        }
    }
}

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onToggle: () -> Unit
) {
    if (isFollowing) {
        Button(
            onClick = onToggle,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Following",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = onToggle,
            modifier = Modifier.height(34.dp),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, BluePrimary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = BluePrimary,
                containerColor = MaterialTheme.colorScheme.surface
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = BluePrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Follow",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NewsAppTheme {
        NotificationScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotificationScreenDarkPreview() {
    NewsAppTheme {
        NotificationScreen(onBackClick = {})
    }
}
