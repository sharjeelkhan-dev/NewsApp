package com.sharjeel.newsapp.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val country: String = "",
    val topics: List<String> = emptyList(),
    val sources: List<String> = emptyList(),
    val bio: String = "",
    val website: String = ""
)
