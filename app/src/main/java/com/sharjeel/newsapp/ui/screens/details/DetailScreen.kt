package com.sharjeel.newsapp.ui.screens.details

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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.components.AppScaffold
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: com.sharjeel.newsapp.domain.model.Article? = null,
    onBackClick: () -> Unit,
    onCommentClick: () -> Unit,
    onAuthorClick: (String) -> Unit = {},
    viewModel: DetailViewModel = hiltViewModel()
) {
    val aiSummary by viewModel.aiSummary
    val aiTranslation by viewModel.aiTranslation
    val aiSentiment by viewModel.aiSentiment
    val aiEnhancedHeadline by viewModel.aiEnhancedHeadline
    
    val isSummarizing by viewModel.isSummarizing
    val isTranslating by viewModel.isTranslating
    val isAnalyzingSentiment by viewModel.isAnalyzingSentiment
    val isEnhancingHeadline by viewModel.isEnhancingHeadline

    var isFollowing by remember { mutableStateOf(true) }
    var isLiked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(true) }

    var fullArticleText by remember { mutableStateOf("") }
    var isContentLoading by remember { mutableStateOf(false) }

    val logoDomain = remember(article?.url) { article?.url?.let { TimeUtils.getDomain(it) } }
    val logoUrl = if (!logoDomain.isNullOrBlank()) "https://www.google.com/s2/favicons?sz=128&domain=$logoDomain" else ""

    LaunchedEffect(article) {
        article?.let { viewModel.setArticle(it) }
    }

    LaunchedEffect(article?.url) {
        if (!article?.url.isNullOrBlank()) {
            isContentLoading = true
            fullArticleText = withContext(Dispatchers.IO) {
                try {
                    val doc = Jsoup.connect(article.url)
                        .userAgent("Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36")
                        .timeout(15000)
                        .followRedirects(true)
                        .get()

                    doc.select("script, style, header, footer, nav, iframe, noscript, .ads, .advertisement, .comments, #comments").remove()

                    var extractedText = extractTextFromSelectors(doc, "article, [itemprop=articleBody], .article-body, .story-content, .entry-content, .post-content")

                    if (extractedText.isBlank() || extractedText.length < 300) {
                        val paragraphs = doc.select("p")
                        extractedText = paragraphs.map { it.text().trim() }
                            .filter { it.isNotEmpty() && it.length > 20 }
                            .joinToString(separator = "\n\n")
                    }

                    val finalCleanText = extractedText.replace(Regex("\\[\\+\\d+\\s+chars]"), "").trim()

                    if (finalCleanText.length > 150) {
                        finalCleanText
                    } else {
                        cleanFallbackText(article.content)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    cleanFallbackText(article.content)
                }
            }
            isContentLoading = false
        }
    }

    AppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            // Top Row Setup (Original)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Share */ }, modifier = Modifier.size(26.dp)) {
                    Icon(painter = painterResource(id = R.drawable.share_line_icon), contentDescription = "Share", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = { /* More */ }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onSurface)
                }
            }

            // Scrollable News Body Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                // Identity Header Section (Original)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val sourceClickModifier = Modifier.clickable { article?.sourceId?.let { onAuthorClick(it) } }
                    
                    if (logoUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(logoUrl)
                                .crossfade(true)
                                .error(R.drawable.ic_launcher_foreground)
                                .fallback(R.drawable.ic_launcher_foreground)
                                .build(),
                            contentDescription = "Source Logo",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .then(sourceClickModifier),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFBB1919))
                                .then(sourceClickModifier), 
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = (article?.sourceName ?: "N").take(1).uppercase(), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f).then(sourceClickModifier)) {
                        Text(text = article?.sourceName ?: "Unknown Source", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = article?.publishedAt?.let { TimeUtils.formatRelativeTime(it) } ?: "Just now", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp))
                    }
                    if (isFollowing) {
                        Button(onClick = { isFollowing = false }, shape = RoundedCornerShape(6.dp), colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = Color.White), modifier = Modifier.height(34.dp), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)) {
                            Text("Following", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp))
                        }
                    } else {
                        OutlinedButton(onClick = { isFollowing = true }, shape = RoundedCornerShape(6.dp), border = androidx.compose.foundation.BorderStroke(1.dp, BluePrimary), colors = ButtonDefaults.outlinedButtonColors(contentColor = BluePrimary), modifier = Modifier.height(34.dp), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)) {
                            Text("Follow", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Feature Media Box (Original)
                Card(modifier = Modifier.fillMaxWidth().height(220.dp), shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(0.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article?.urlToImage?.ifEmpty { R.drawable.newsimages } ?: R.drawable.newsimages)
                            .crossfade(true).placeholder(R.drawable.newsimages).error(R.drawable.newsimages).fallback(R.drawable.newsimages).build(),
                        contentDescription = "Article Image", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = article?.author?.ifBlank { "News" } ?: "News", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                
                // Title with AI Enhancement
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = aiEnhancedHeadline ?: article?.title ?: "No Title Available",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 22.sp,
                            lineHeight = 30.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    if (aiEnhancedHeadline == null) {
                        IconButton(onClick = { article?.title?.let { viewModel.enhanceHeadline(it) } }) {
                            if (isEnhancingHeadline) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = BluePrimary)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Enhance Title",
                                    tint = BluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Smart AI Chips Row
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AIChip(
                        label = "Summarize",
                        icon = Icons.Default.Bolt,
                        isLoading = isSummarizing,
                        onClick = { viewModel.summarizeArticle(fullArticleText) }
                    )
                    AIChip(
                        label = "Translate",
                        icon = Icons.Default.Translate,
                        isLoading = isTranslating,
                        onClick = { viewModel.translateArticle(fullArticleText, "Urdu") }
                    )
                    AIChip(
                        label = "Analysis",
                        icon = Icons.Default.Analytics,
                        isLoading = isAnalyzingSentiment,
                        onClick = { viewModel.analyzeSentiment(fullArticleText) }
                    )
                }

                // AI Insights Card
                if (aiSummary != null || aiTranslation != null || aiSentiment != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AIResultCard(
                        summary = aiSummary,
                        translation = aiTranslation,
                        sentiment = aiSentiment
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Complete Dynamic UI News Stream
                if (isContentLoading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BluePrimary)
                    }
                } else {
                    Text(
                        text = fullArticleText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 26.sp,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            // Bottom Actions Footer Fixed Block (Original)
            Row(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.clickable { isLiked = !isLiked }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Likes", tint = if (isLiked) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isLiked) "24.6K" else "24.5K", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp))
                }
                Spacer(modifier = Modifier.width(28.dp))
                Row(modifier = Modifier.clickable { onCommentClick() }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.talk_bubble_icon), contentDescription = "Comments", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "1K", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { isBookmarked = !isBookmarked }, modifier = Modifier.size(24.dp)) {
                    Icon(painter = painterResource(id = R.drawable.saved_bookmark_icon), contentDescription = "Bookmark", tint = if (isBookmarked) BluePrimary else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun AIChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    ElevatedAssistChip(
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
        leadingIcon = {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = BluePrimary)
            } else {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = BluePrimary)
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = AssistChipDefaults.elevatedAssistChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        enabled = !isLoading
    )
}

@Composable
fun AIResultCard(
    summary: String?,
    translation: String?,
    sentiment: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI INSIGHTS", style = MaterialTheme.typography.labelSmall.copy(color = BluePrimary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp))
            }
            
            if (sentiment != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Sentiment: $sentiment",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (sentiment.contains("Positive", true)) Color(0xFF4CAF50) else if (sentiment.contains("Negative", true)) Color(0xFFF44336) else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (summary != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("AI Summary", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(summary, style = MaterialTheme.typography.bodyMedium)
            }

            if (translation != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Urdu Translation", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(translation, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun extractTextFromSelectors(doc: Document, selectors: String): String {
    val elements = doc.select(selectors)
    for (element in elements) {
        val paras = element.select("p")
        if (paras.isNotEmpty()) {
            val content = paras.joinToString(separator = "\n\n") { it.text().trim() }
            if (content.length > 200) return content
        }
    }
    return ""
}

private fun cleanFallbackText(text: String): String {
    return text.replace(Regex("\\[\\+\\d+\\s+chars]"), "").replace("...", "").trim()
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NewsAppTheme {
        DetailScreen(onBackClick = {}, onCommentClick = {})
    }
}
