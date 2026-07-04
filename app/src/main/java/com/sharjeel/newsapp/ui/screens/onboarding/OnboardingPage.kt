package com.sharjeel.newsapp.ui.screens.onboarding

import androidx.annotation.DrawableRes
import com.sharjeel.newsapp.R

data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Track the Future of Finance",
        description = "Stay ahead of the curve with real-time updates on cryptocurrency, global markets, and breaking tech trends that shape tomorrow's economy.",
        image = R.drawable.newsimages
    ),
    OnboardingPage(
        title = "Explore the World From Here",
        description = "Discover breathtaking destinations, rich cultural insights, and global stories that bring the beauty and diversity of our planet right to your screen.",
        image = R.drawable.newsimages2
    ),
    OnboardingPage(
        title = "Fuel Your Mind and Body",
        description = "Elevate your daily routine with curated news on wellness, healthy living, and culinary inspiration designed to help you live your best life.",
        image = R.drawable.newsimages3
    )
)
