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
}
