package com.sharjeel.newsapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.sharjeel.newsapp.data.remote.NewsApi
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
    private val api: NewsApi,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : NewsRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getTopHeadlines(category: String?): Result<List<Article>> {
        return try {
            val response = api.getTopHeadlines(category = category)
            Result.success(response.articles.map { it.toArticle() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNewsByInterests(interests: List<String>): Result<List<Article>> {
        return try {
            // NewsAPI doesn't support multiple categories in one call, 
            // so we query 'everything' with interests as keywords or fetch top for each
            val query = interests.joinToString(" OR ")
            val response = api.getEverything(query = query)
            Result.success(response.articles.map { it.toArticle() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNewsBySource(sourceId: String): Result<List<Article>> {
        return try {
            val response = api.getTopHeadlines(sources = sourceId)
            Result.success(response.articles.map { it.toArticle() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllSources(): Result<List<NewsSource>> {
        return try {
            val response = api.getSources()
            Result.success(response.sources.map { source ->
                NewsSource(
                    id = source.id,
                    name = source.name,
                    description = source.description,
                    category = source.category,
                    url = source.url
                )
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun followSource(sourceId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .update("followedSources", FieldValue.arrayUnion(sourceId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unfollowSource(sourceId: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            usersCollection.document(uid)
                .update("followedSources", FieldValue.arrayRemove(sourceId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFollowedSources(): Flow<List<String>> {
        val uid = auth.currentUser?.uid ?: return emptyFlow()
        return usersCollection.document(uid).snapshots().map { snapshot ->
            @Suppress("UNCHECKED_CAST")
            (snapshot.get("followedSources") as? List<String>) ?: emptyList()
        }
    }

    private fun com.sharjeel.newsapp.data.remote.dto.ArticleDto.toArticle() = Article(
        title = title,
        description = description ?: "",
        content = content ?: "",
        url = url,
        urlToImage = urlToImage ?: "",
        publishedAt = publishedAt,
        author = author ?: "Unknown",
        sourceName = source.name,
        sourceId = source.id ?: ""
    )
}
