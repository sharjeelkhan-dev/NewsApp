package com.sharjeel.newsapp.domain.repository

import com.sharjeel.newsapp.domain.model.Article
import com.sharjeel.newsapp.domain.model.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getTopHeadlines(category: String? = null): Result<List<Article>>
    suspend fun getNewsByInterests(interests: List<String>): Result<List<Article>>
    suspend fun getNewsBySource(sourceId: String): Result<List<Article>>
    suspend fun getAllSources(): Result<List<NewsSource>>
    suspend fun followSource(sourceId: String): Result<Unit>
    suspend fun unfollowSource(sourceId: String): Result<Unit>
    fun getFollowedSources(): Flow<List<String>>

    // Bookmark Features
    suspend fun bookmarkArticle(article: Article): Result<Unit>
    suspend fun removeBookmark(articleUrl: String): Result<Unit>
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun isArticleBookmarked(articleUrl: String): Boolean

    // Content Preferences & Moderation
    suspend fun hideArticle(articleUrl: String): Result<Unit>
    suspend fun blockSource(sourceId: String): Result<Unit>
    suspend fun reportArticle(articleUrl: String, reason: String): Result<Unit>
    fun getHiddenArticles(): Flow<List<String>>
    fun getBlockedSources(): Flow<List<String>>
}
