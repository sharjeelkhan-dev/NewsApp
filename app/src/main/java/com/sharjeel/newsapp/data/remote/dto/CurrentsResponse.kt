package com.sharjeel.newsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentsResponse(
    @Json(name = "status") val status: String?,
    @Json(name = "news") val news: List<CurrentsArticleDto>?
)

@JsonClass(generateAdapter = true)
data class CurrentsArticleDto(
    @Json(name = "id") val id: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "author") val author: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "language") val language: String?,
    @Json(name = "category") val category: List<String>?,
    @Json(name = "published") val published: String?
)
