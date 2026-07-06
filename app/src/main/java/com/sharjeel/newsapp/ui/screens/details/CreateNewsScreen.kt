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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun CreateNewsScreen(
    onBackClick: () -> Unit,
    onPublishClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var article by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Int?>(null) } // Using drawable ID for mock

    // State for toolbar selections
    var isBold by remember { mutableStateOf(false) }
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
            // 1. Fixed Header Section
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

                // Image Picker / Display Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(183.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .then(
                            if (imageUri == null) {
                                val strokeColor = MaterialTheme.colorScheme.onSurfaceVariant
                                Modifier.drawBehind {
                                    drawRoundRect(
                                        color = strokeColor,
                                        style = Stroke(
                                            width = 3.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )
                                    )
                                }
                            } else Modifier
                        )
                        .clickable { imageUri = R.drawable.newsimages },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add Cover Photo",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = imageUri!!),
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
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }

                // News Title Input
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text("News title",
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 20.sp)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                // Article Input
                TextField(
                    value = article,
                    onValueChange = { article = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    placeholder = { 
                        Text("Add News/Article", color = MaterialTheme.colorScheme
                            .onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 16.sp)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                )
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
                    // Grouped tools (Bold, Italic, etc) - Mock UI
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme
                            .colorScheme.outline.copy(alpha = 0.1f))
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
                        onClick = onPublishClick,
                        modifier = Modifier.height(48.dp).width(109.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (title.isNotEmpty()) BluePrimary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (title.isNotEmpty()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
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
fun CreateNewsScreenPreview() {
    NewsAppTheme {
        CreateNewsScreen(onBackClick = {}, onPublishClick = {})
    }
}
