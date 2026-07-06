package com.sharjeel.newsapp.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.sharjeel.newsapp.ui.components.ToolbarIcon
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishedNewsDetailScreen(
    onBackClick: () -> Unit
) {
    // State for toolbar selections
    var isBold by remember { mutableStateOf(true) }
    var isItalic by remember { mutableStateOf(false) }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            // 1. Header Section
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
                    text = "Create News",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                IconButton(
                    onClick = { /* More options */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // 2. Scrollable Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Image Display Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(248.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.newsimages),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Edit icon overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { /* Change image */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.illustration_icon),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // News Title
                Text(
                    text = "Healthy Living: Diet and Exercise Tips & Tools for Success",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        lineHeight = 32.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                // Article Content
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec venenatis lectus, et dapibus eros. Praesent id magna quis purus pharetra scelerisque ut quis felis. Duis dictum efficitur purus et blandit. Duis vel consequat dui. Nullam euismod, nisl eu fermentum convallis, arcu lectus sagittis ipsum, in elementum tortor purus vitae ligula. Mauris at enim elementum, laoreet metus sit amet, cursus orci. Sed nec elit libero. In accumsan mi non sollicitudin tincidunt. Proin molestie orci id pulvinar placerat.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. Fixed Bottom Tools & Publish Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Formatting Toolbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    ) {
                        Row(modifier = Modifier.padding(8.dp)) {
                            ToolbarIcon(
                                painter = painterResource(id = R.drawable.vector__4_),
                                isSelected = isBold,
                                onClick = { isBold = !isBold }
                            ) // Bold
                            ToolbarIcon(
                                painter = painterResource(id = R.drawable.vector__3_),
                                isSelected = isItalic,
                                onClick = { isItalic = !isItalic }
                            ) // Italic
                            ToolbarIcon(
                                painter = painterResource(id = R.drawable.vector__1_),
                                onClick = { /* TODO */ }
                            ) // Bullet
                            ToolbarIcon(
                                painter = painterResource(id = R.drawable.vector__2_),
                                onClick = { /* TODO */ }
                            ) // Numbered
                            ToolbarIcon(
                                painter = painterResource(id = R.drawable.link),
                                onClick = { /* TODO */ }
                            ) // Link
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
                            Icon(painterResource(id = R.drawable.icon__2_), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
                            Icon(painterResource(id = R.drawable.icon__1_), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
                            Icon(painterResource(id = R.drawable.icon), contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Button(
                        onClick = { /* Already Published */ },
                        modifier = Modifier.height(48.dp).width(109.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Publish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PublishedNewsDetailScreenPreview() {
    NewsAppTheme {
        PublishedNewsDetailScreen(onBackClick = {})
    }
}

