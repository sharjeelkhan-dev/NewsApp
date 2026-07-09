package com.sharjeel.newsapp.domain.model

data class Article(
    val title: String = "",
    val description: String = "",
    val content: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = "",
    val author: String = "",
    val sourceName: String = "",
    val sourceId: String = ""
)

data class NewsSource(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val url: String = "",
    val isFollowed: Boolean = false
)
