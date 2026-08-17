package com.sharjeel.newsapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.sharjeel.newsapp.data.remote.CurrentsApi
import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.model.NewsSource
import com.sharjeel.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
            android.util.Log.d("NewsRepo", "Fetching headlines for category: $category")

            val combinedArticles = mutableListOf<Article>()

            // If category is provided and not "All"/"general", search by category
            if (category != null && category.lowercase() != "all" && category.lowercase() != "general") {
                combinedArticles.addAll(fetchSearchFromCurrents(category))
            } else {
                combinedArticles.addAll(fetchFromCurrents())
            }

            // Final fallback to Mock Data if empty
            if (combinedArticles.isEmpty()) {
                android.util.Log.e("NewsRepo", "Currents API returned no news. Loading Mock Data.")
                combinedArticles.addAll(getMockArticles())
            }

            val finalArticles = combinedArticles.distinctBy { it.title }.take(50)

            android.util.Log.d("NewsRepo", "Returning ${finalArticles.size} articles")
            Result.success(finalArticles)
        } catch (e: Exception) {
            android.util.Log.e("NewsRepo", "getTopHeadlines fatal error: ${e.message}", e)
            Result.success(getMockArticles())
        }
    }

    private suspend fun fetchFromCurrents(): List<Article> {
        android.util.Log.d("NewsRepo", "fetchFromCurrents: Calling API")
        return try {
            val response = currentsApi.getLatestNews(
                language = "en",
                apiKey = CurrentsApi.API_KEY
            )
            android.util.Log.d("NewsRepo", "fetchFromCurrents: API Response Status: ${response.status}")
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
            
            if (articles.isEmpty()) {
                android.util.Log.d("NewsRepo", "fetchFromCurrents: API returned empty, using mock")
                getMockArticles()
            } else {
                android.util.Log.d("NewsRepo", "fetchFromCurrents: API returned ${articles.size} articles")
                articles
            }
        } catch (e: Exception) {
            android.util.Log.e("NewsRepo", "fetchFromCurrents: ERROR: ${e.message}", e)
            getMockArticles()
        }
    }

    private fun getMockArticles(): List<Article> {
        return listOf(
            Article(
                title = "Global Tech Innovation Summit 2024: The Future of AI and Robotics",
                description = "Leading experts from around the world gather to discuss the transformative impact of artificial intelligence on global industries and society.",
                content = "The Global Tech Innovation Summit 2024 has officially kicked off in San Francisco, bringing together the brightest minds in technology and science...",
                url = "https://example.com/tech1",
                urlToImage = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e",
                publishedAt = "2024-03-20T10:00:00Z",
                author = "Sarah Johnson",
                sourceName = "Tech World",
                sourceId = "tech-world"
            ),
            Article(
                title = "Sustainable Energy Breakthrough: New Hydrogen Fuel Cells for Clean Transport",
                description = "Researchers announce a major milestone in clean energy technology, paving the way for emission-free heavy transport and aviation.",
                content = "In a significant leap towards a greener future, scientists have developed a new generation of hydrogen fuel cells that are 30% more efficient...",
                url = "https://example.com/science1",
                urlToImage = "https://images.unsplash.com/photo-1509391366360-fe5bb658582f",
                publishedAt = "2024-03-20T11:30:00Z",
                author = "Dr. Robert Chen",
                sourceName = "Scientific Journal",
                sourceId = "science-journal"
            ),
            Article(
                title = "Major Sports Update: Upcoming Championship Finals Preview",
                description = "All eyes are on the upcoming finals as teams prepare for the ultimate showdown in the world of professional sports.",
                content = "With the championship finals just around the corner, excitement is at an all-time high. Both teams have shown incredible form throughout the season...",
                url = "https://example.com/sports1",
                urlToImage = "https://images.unsplash.com/photo-1504450758481-7338eba7524a",
                publishedAt = "2024-03-20T14:45:00Z",
                author = "James Miller",
                sourceName = "Sports Daily",
                sourceId = "sports-daily"
            ),
            Article(
                title = "Global Economy Trends: Navigating Market Volatility in 2024",
                description = "Financial analysts provide insights into the current state of the global economy and strategies for investors to manage market changes.",
                content = "The global economy is currently facing a period of significant change. Rising interest rates and geopolitical tensions have led to increased market volatility...",
                url = "https://example.com/business1",
                urlToImage = "https://images.unsplash.com/photo-1460925895917-afdab827c52f",
                publishedAt = "2024-03-20T09:15:00Z",
                author = "Elena Rodriguez",
                sourceName = "Market Insider",
                sourceId = "market-insider"
            )
        )
    }

    override suspend fun getNewsByInterests(interests: List<String>): Result<List<Article>> {
        return try {
            android.util.Log.d("NewsRepo", "Fetching news from Currents for interests: $interests")

            val combinedArticles = mutableListOf<Article>()

            if (interests.isNotEmpty()) {
                for (interest in interests.take(5)) {
                    combinedArticles.addAll(fetchSearchFromCurrents(interest))
                }
            } else {
                combinedArticles.addAll(fetchFromCurrents())
            }

            if (combinedArticles.isEmpty()) {
                android.util.Log.e("NewsRepo", "Interests Search Failed. Loading Professional Mock Data.")
                combinedArticles.addAll(getMockArticles())
            }

            val finalArticles = combinedArticles.distinctBy { it.title }.take(50)
            Result.success(finalArticles)
        } catch (e: Exception) {
            android.util.Log.e("NewsRepo", "getNewsByInterests error: ${e.message}", e)
            Result.success(getMockArticles())
        }
    }

    private suspend fun fetchSearchFromCurrents(query: String): List<Article> {
        android.util.Log.d("NewsRepo", "fetchSearchFromCurrents: Calling API for query: $query")
        return try {
            // Parameter name changed from keywords to query to fix IDE error
            val response = currentsApi.searchNews(
                query = query,
                language = "en",
                apiKey = CurrentsApi.API_KEY
            )
            android.util.Log.d("NewsRepo", "fetchSearchFromCurrents: API Response Status: ${response.status}")
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

            if (articles.isEmpty()) {
                android.util.Log.d("NewsRepo", "fetchSearchFromCurrents: API returned empty for $query, using mock")
                getMockArticles()
            } else {
                android.util.Log.d("NewsRepo", "fetchSearchFromCurrents: API returned ${articles.size} articles for $query")
                articles
            }
        } catch (e: Exception) {
            android.util.Log.e("NewsRepo", "fetchSearchFromCurrents: ERROR for $query: ${e.message}", e)
            getMockArticles()
        }
    }

    override suspend fun getNewsBySource(sourceId: String): Result<List<Article>> {
        return try {
            // Parameter name changed from keywords to query to fix IDE error
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
            
            // Run as a transaction to ensure both user list and source count are updated
            firestore.runTransaction { transaction ->
                val userRef = usersCollection.document(uid)
                val sourceRef = sourcesCollection.document(sourceId)
                
                transaction.update(userRef, "followedSources", FieldValue.arrayUnion(sourceId))
                transaction.update(sourceRef, "followerCount", FieldValue.increment(1))
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            // Fallback: If source document doesn't exist, just update user (optional: create source doc)
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
        } catch (e: Exception) {
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
        } catch (e: Exception) {
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

    override suspend fun askAiAssistant(query: String, context: String?): Result<String> {
        return try {
            val systemPrompt = "You are a smart News Assistant for the 'Akhbar' app. Help the user with their news-related questions."
            val fullPrompt = if (context != null) {
                "$systemPrompt\n\nContext about the current article:\n$context\n\nUser Question: $query"
            } else {
                "$systemPrompt\n\nUser Question: $query"
            }
            val response = generativeModel.generateContent(fullPrompt)
            Result.success(response.text ?: "I'm sorry, I couldn't process that request.")
        } catch (e: Exception) {
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
