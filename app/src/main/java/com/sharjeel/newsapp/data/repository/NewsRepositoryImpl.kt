package com.sharjeel.newsapp.data.repository

import android.util.Log
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.sharjeel.newsapp.data.remote.CurrentsApi
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.model.NewsSource
import com.sharjeel.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val currentsApi: CurrentsApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val generativeModel: GenerativeModel
) : NewsRepository {

    private val usersCollection = firestore.collection("users")
    private val sourcesCollection = firestore.collection("sources")

    override suspend fun getTopHeadlines(category: String?): Result<List<Article>> {
        return try {
            Log.d("NewsRepo", "Fetching headlines for category: $category")

            val combinedArticles = mutableListOf<Article>()

            if (category != null && category.lowercase() != "all" && category.lowercase() != "general") {
                combinedArticles.addAll(fetchSearchFromCurrents(category))
            } else {
                combinedArticles.addAll(fetchFromCurrents())
            }

            if (combinedArticles.isEmpty()) {
                Log.e("NewsRepo", "Currents API returned no news. Loading Mock Data.")
                combinedArticles.addAll(getMockArticles())
            }

            val finalArticles = combinedArticles.distinctBy { it.title }.take(50)
            Log.d("NewsRepo", "Returning ${finalArticles.size} articles")
            Result.success(finalArticles)
        } catch (e: Exception) {
            Log.e("NewsRepo", "getTopHeadlines fatal error: ${e.message}", e)
            Result.success(getMockArticles())
        }
    }

    private suspend fun fetchFromCurrents(): List<Article> {
        Log.d("NewsRepo", "fetchFromCurrents: Calling API")
        return try {
            val response = currentsApi.getLatestNews(
                language = "en",
                apiKey = CurrentsApi.API_KEY
            )
            val articles = response.news?.map { dto ->
                val safeImage = dto.image?.let {
                    if (it == "None" || it.isEmpty()) ""
                    else if (it.startsWith("http://")) it.replace("http://", "https://")
                    else it
                } ?: ""

                Article(
                    title = dto.title ?: "Latest News Update",
                    description = dto.description ?: "",
                    content = dto.description ?: "",
                    url = dto.url ?: "",
                    urlToImage = safeImage,
                    publishedAt = dto.published ?: "Just now",
                    author = dto.author ?: "Unknown",
                    sourceName = dto.author ?: "Currents API",
                    sourceId = ""
                )
            } ?: emptyList()

            articles
        } catch (e: Exception) {
            Log.e("NewsRepo", "fetchFromCurrents: ERROR: ${e.message}", e)
            emptyList()
        }
    }

    private fun getMockArticles(): List<Article> {
        return listOf(
            Article(
                title = "Global Tech Innovation Summit: The Future of AI and Robotics",
                description = "Leading experts gather to discuss the transformative impact of artificial intelligence on global industries.",
                content = "The Global Tech Innovation Summit has officially kicked off, bringing together top minds in technology...",
                url = "https://example.com/tech1",
                urlToImage = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e",
                publishedAt = "Just now",
                author = "Sarah Johnson",
                sourceName = "Tech World",
                sourceId = "tech-world"
            )
        )
    }

    override suspend fun getNewsByInterests(interests: List<String>): Result<List<Article>> {
        return try {
            val combinedArticles = mutableListOf<Article>()
            if (interests.isNotEmpty()) {
                for (interest in interests.take(5)) {
                    combinedArticles.addAll(fetchSearchFromCurrents(interest))
                }
            } else {
                combinedArticles.addAll(fetchFromCurrents())
            }

            if (combinedArticles.isEmpty()) {
                combinedArticles.addAll(getMockArticles())
            }

            val finalArticles = combinedArticles.distinctBy { it.title }.take(50)
            Result.success(finalArticles)
        } catch (_: Exception) {
            Result.success(getMockArticles())
        }
    }

    private suspend fun fetchSearchFromCurrents(query: String): List<Article> {
        Log.d("NewsRepo", "fetchSearchFromCurrents: Calling API for query: $query")
        return try {
            val response = currentsApi.searchNews(
                query = query,
                language = "en",
                apiKey = CurrentsApi.API_KEY
            )
            val articles = response.news?.map { dto ->
                val safeImage = dto.image?.let {
                    if (it == "None" || it.isEmpty()) ""
                    else if (it.startsWith("http://")) it.replace("http://", "https://")
                    else it
                } ?: ""

                Article(
                    title = dto.title ?: "Search Results",
                    description = dto.description ?: "",
                    content = dto.description ?: "",
                    url = dto.url ?: "",
                    urlToImage = safeImage,
                    publishedAt = dto.published ?: "Just now",
                    author = dto.author ?: "Currents Search",
                    sourceName = dto.author ?: "Currents Search",
                    sourceId = ""
                )
            } ?: emptyList()

            articles
        } catch (e: Exception) {
            Log.e("NewsRepo", "fetchSearchFromCurrents ERROR for $query: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getNewsBySource(sourceId: String): Result<List<Article>> {
        return try {
            val response = currentsApi.searchNews(
                query = sourceId,
                language = "en",
                apiKey = CurrentsApi.API_KEY
            )
            val articles = response.news?.map { dto ->
                Article(
                    title = dto.title ?: "Source Update",
                    description = dto.description ?: "",
                    content = dto.description ?: "",
                    url = dto.url ?: "",
                    urlToImage = dto.image ?: "",
                    publishedAt = dto.published ?: "",
                    author = dto.author ?: sourceId,
                    sourceName = dto.author ?: sourceId,
                    sourceId = sourceId
                )
            } ?: emptyList()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllSources(): Result<List<NewsSource>> {
        return Result.success(getDefaultSources())
    }

    private fun getDefaultSources(): List<NewsSource> {
        return listOf(
            NewsSource("bbc-news", "BBC News", "Breaking news, family, and global analysis.", "General", "https://www.bbc.co.uk/news"),
            NewsSource("cnn", "CNN", "World news and international headlines.", "General", "https://edition.cnn.com"),
            NewsSource("reuters", "Reuters", "Factual and unbiased global news coverage.", "General", "https://www.reuters.com"),
            NewsSource("techcrunch", "TechCrunch", "The latest technology news and analysis.", "Technology", "https://techcrunch.com")
        )
    }

    override suspend fun followSource(sourceId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.runTransaction { transaction ->
                val userRef = usersCollection.document(uid)
                val sourceRef = sourcesCollection.document(sourceId)
                transaction.update(userRef, "followedSources", FieldValue.arrayUnion(sourceId))
                transaction.update(sourceRef, "followerCount", FieldValue.increment(1))
            }.await()
            Result.success(Unit)
        } catch (_: Exception) {
            try {
                val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
                usersCollection.document(uid)
                    .update("followedSources", FieldValue.arrayUnion(sourceId))
                    .await()
                Result.success(Unit)
            } catch (inner: Exception) {
                Result.failure(inner)
            }
        }
    }

    override suspend fun unfollowSource(sourceId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.runTransaction { transaction ->
                val userRef = usersCollection.document(uid)
                val sourceRef = sourcesCollection.document(sourceId)
                transaction.update(userRef, "followedSources", FieldValue.arrayRemove(sourceId))
                transaction.update(sourceRef, "followerCount", FieldValue.increment(-1))
            }.await()
            Result.success(Unit)
        } catch (_: Exception) {
            try {
                val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
                usersCollection.document(uid)
                    .update("followedSources", FieldValue.arrayRemove(sourceId))
                    .await()
                Result.success(Unit)
            } catch (inner: Exception) {
                Result.failure(inner)
            }
        }
    }

    override fun getFollowedSources(): Flow<List<String>> {
        val uid = auth.currentUser?.uid ?: return emptyFlow()
        return usersCollection.document(uid).snapshots().map { snapshot ->
            @Suppress("UNCHECKED_CAST")
            (snapshot.get("followedSources") as? List<String>) ?: emptyList()
        }
    }

    override suspend fun bookmarkArticle(article: Article): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .collection("bookmarks")
                .document(android.util.Base64.encodeToString(article.url.toByteArray(), android.util.Base64.NO_WRAP))
                .set(article)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeBookmark(articleUrl: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .collection("bookmarks")
                .document(android.util.Base64.encodeToString(articleUrl.toByteArray(), android.util.Base64.NO_WRAP))
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        val uid = auth.currentUser?.uid ?: return emptyFlow()
        return usersCollection.document(uid)
            .collection("bookmarks")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Article::class.java)
            }
    }

    override suspend fun isArticleBookmarked(articleUrl: String): Boolean {
        return try {
            val uid = auth.currentUser?.uid ?: return false
            val doc = usersCollection.document(uid)
                .collection("bookmarks")
                .document(android.util.Base64.encodeToString(articleUrl.toByteArray(), android.util.Base64.NO_WRAP))
                .get()
                .await()
            doc.exists()
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun hideArticle(articleUrl: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .update("hiddenArticles", FieldValue.arrayUnion(articleUrl))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun blockSource(sourceId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .update("blockedSources", FieldValue.arrayUnion(sourceId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reportArticle(articleUrl: String, reason: String): Result<Unit> {
        return try {
            firestore.collection("reports").add(
                mapOf(
                    "articleUrl" to articleUrl,
                    "reason" to reason,
                    "reportedBy" to (auth.currentUser?.uid ?: "anonymous"),
                    "timestamp" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getHiddenArticles(): Flow<List<String>> {
        val uid = auth.currentUser?.uid ?: return emptyFlow()
        return usersCollection.document(uid).snapshots().map { snapshot ->
            @Suppress("UNCHECKED_CAST")
            (snapshot.get("hiddenArticles") as? List<String>) ?: emptyList()
        }
    }

    override fun getBlockedSources(): Flow<List<String>> {
        val uid = auth.currentUser?.uid ?: return emptyFlow()
        return usersCollection.document(uid).snapshots().map { snapshot ->
            @Suppress("UNCHECKED_CAST")
            (snapshot.get("blockedSources") as? List<String>) ?: emptyList()
        }
    }

    // AI Implementations
    override suspend fun summarizeArticle(content: String): Result<String> {
        return try {
            val prompt = "Summarize the following news article in exactly 3 concise bullet points:\n\n$content"
            val response = generativeModel.generateContent(prompt)
            Result.success(response.text ?: "No summary generated.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun translateArticle(content: String, targetLanguage: String): Result<String> {
        return try {
            val prompt = "Translate the following news article into $targetLanguage. Keep the tone professional:\n\n$content"
            val response = generativeModel.generateContent(prompt)
            Result.success(response.text ?: "Translation failed.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeSentiment(content: String): Result<String> {
        return try {
            val prompt = "Analyze the sentiment of this news article. Respond with only one word: Positive, Negative, or Neutral.\n\n$content"
            val response = generativeModel.generateContent(prompt)
            Result.success(response.text ?: "Neutral")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractSearchKeywords(query: String): String {
        val stopWords = setOf(
            "who", "what", "where", "when", "why", "how", "is", "are", "was", "were", "the", "a", "an",
            "kon", "kya", "hai", "hain", "ka", "ki", "ke", "ko", "par", "me", "tell", "me", "about",
            "give", "latest", "news", "updates", "regarding", "info", "details"
        )
        val cleaned = query.lowercase(Locale.ROOT)
            .replace(Regex("[^a-zA-Z0-9\\s]"), "")
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() && !stopWords.contains(it) }
            .joinToString(" ")

        return cleaned.ifBlank { query }
    }

    override suspend fun askAiAssistant(query: String, context: String?): Result<String> {
        return try {
            val relevantContext = if (context == null) {
                val cleanKeywords = extractSearchKeywords(query)
                Log.d("AkhbarAI", "Extracted search keywords: '$cleanKeywords' from query: '$query'")

                var searchResults = fetchSearchFromCurrents(cleanKeywords).take(10)

                if (searchResults.isEmpty()) {
                    Log.d("AkhbarAI", "Keyword search empty. Fetching latest top headlines as context.")
                    searchResults = fetchFromCurrents().take(10)
                }

                if (searchResults.isNotEmpty()) {
                    searchResults.joinToString("\n") { article ->
                        "- Title: ${article.title}\n  Summary: ${article.description}\n  Source: ${article.sourceName}\n  Date: ${article.publishedAt}"
                    }
                } else null
            } else null

            val currentDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())

            val systemPrompt = """
                You are 'Akhbar AI', a high-intelligence, globally aware assistant. 
                You have two powerful capabilities:
                1. WORLDWIDE KNOWLEDGE: You can answer ANY question about history, science, tech, celebrities, geography, or general knowledge from across the globe.
                2. LIVE NEWS AWARENESS: You are integrated with the 'Akhbar News App' and have access to the latest live news feed (provided below).

                RESPONSE GUIDELINES:
                - ALWAYS answer the user's question directly and comprehensively using your full worldwide knowledge.
                - INTEGRATION: If the user's question relates to current events, cross-reference your worldwide knowledge with the 'RECENT NEWS FEED CONTEXT' provided below to give the most up-to-date and accurate answer.
                - If the query is just a general fact (e.g. "Who is the President of France?"), answer it immediately.
                - If the query is about news (e.g. "What happened in the stock market today?"), use the provided news context and mention the source/date from the context.
                - Be factual, polite, and reply in the EXACT language used by the user (English, Urdu, or Roman Urdu).
                - DO NOT say "I am just a news assistant" or "I don't have information". If you know it, say it.
                
                Current System Date: $currentDate
            """.trimIndent()

            val fullPrompt = buildString {
                append(systemPrompt)
                if (!relevantContext.isNullOrBlank()) {
                    append("\n\n=== RECENT NEWS FEED CONTEXT (FROM AKHBAR APP) ===\n")
                    append(relevantContext)
                }
                if (!context.isNullOrBlank()) {
                    append("\n\n=== SPECIFIC ARTICLE CONTEXT ===\n")
                    append(context)
                }
                append("\n\nUSER QUESTION: ")
                append(query)
            }

            val response = generativeModel.generateContent(fullPrompt)
            Result.success(response.text ?: "I'm sorry, I couldn't process that request.")
        } catch (e: Exception) {
            Log.e("AkhbarAI", "Error in askAiAssistant: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun enhanceHeadline(title: String): Result<String> {
        return try {
            val prompt = "Rewrite this news headline to be more engaging and catchy, but keep it factual. Respond with ONLY the new headline:\n\n$title"
            val response = generativeModel.generateContent(prompt)
            Result.success(response.text ?: title)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}