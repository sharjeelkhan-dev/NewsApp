package com.sharjeel.newsapp.data.remote

import com.sharjeel.newsapp.data.remote.dto.NewsResponse
import com.sharjeel.newsapp.data.remote.dto.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("country") country: String? = null,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("sources") sources: String? = null,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/top-headlines/sources")
    suspend fun getSources(
        @Query("category") category: String? = null,
        @Query("country") country: String? = null,
        @Query("apiKey") apiKey: String = API_KEY
    ): SourcesResponse

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY = "f5f0b484501a4e1ca14589d892809d43" // Temporary key for setup
    }
}
