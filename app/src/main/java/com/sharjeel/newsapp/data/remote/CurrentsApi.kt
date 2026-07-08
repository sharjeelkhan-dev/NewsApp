package com.sharjeel.newsapp.data.remote

import com.sharjeel.newsapp.data.remote.dto.CurrentsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentsApi {

    @GET("latest-news")
    suspend fun getLatestNews(
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = API_KEY
    ): CurrentsResponse

    @GET("search")
    suspend fun searchNews(
        @Query("keywords") query: String,
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = API_KEY
    ): CurrentsResponse

    companion object {

        const val BASE_URL = "https://api.currentsapi.services/v1/"

        const val API_KEY = "e0wRUhGm8cd9CWijkHG1akjACVx29EwKNmPA4pUrIlmurR0Q"
    }
}