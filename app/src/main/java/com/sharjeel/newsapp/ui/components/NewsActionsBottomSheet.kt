package com.sharjeel.newsapp.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsActionsBottomSheet(
    onDismissRequest: () -> Unit,
    articleTitle: String,
    articleUrl: String,
    sourceName: String,
    onBookmarkClick: () -> Unit = {},
    onHideClick: () -> Unit = {},
    onBlockSourceClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
    isAlreadyBookmarked: Boolean = false
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header Info
            Text(
                text = articleTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // Actions
            ActionItem(
                icon = if (isAlreadyBookmarked) Icons.Outlined.BookmarkRemove else Icons.Outlined.BookmarkBorder,
                text = if (isAlreadyBookmarked) "Remove from Bookmarks" else "Save / Bookmark for Later",
                onClick = {
                    onBookmarkClick()
                    onDismissRequest()
                }
            )

            ActionItem(
                icon = Icons.Outlined.Share,
                text = "Share Article",
                onClick = {
                    shareArticle(context, articleTitle, articleUrl)
                    onDismissRequest()
                }
            )

            ActionItem(
                icon = Icons.Outlined.ContentCopy,
                text = "Copy Link",
                onClick = {
                    clipboardManager.setText(AnnotatedString(articleUrl))
                    onDismissRequest()
                }
            )

            ActionItem(
                icon = Icons.Outlined.VisibilityOff,
                text = "Hide / Show Less of This",
                onClick = {
                    onHideClick()
                    onDismissRequest()
                }
            )

            ActionItem(
                icon = Icons.Outlined.Block,
                text = "Block / Mute $sourceName",
                onClick = {
                    onBlockSourceClick()
                    onDismissRequest()
                }
            )

            ActionItem(
                icon = Icons.Outlined.Report,
                text = "Report Article",
                contentColor = MaterialTheme.colorScheme.error,
                onClick = {
                    onReportClick()
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    text: String,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )
    }
}

private fun shareArticle(context: Context, title: String, url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "$title\n\nRead more at: $url")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}
