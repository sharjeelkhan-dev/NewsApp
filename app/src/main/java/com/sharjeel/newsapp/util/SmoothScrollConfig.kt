package com.sharjeel.newsapp.util

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Utility to provide optimized scrolling configurations across the app.
 */
object SmoothScrollConfig {

    /**
     * Optimized spring spec for smoother list transitions.
     */
    val ScrollSpringSpec = spring<Float>(
        stiffness = Spring.StiffnessLow,
        visibilityThreshold = 0.1f
    )

    /**
     * Provides a smooth fling behavior that feels more responsive and fluid.
     */
    @Composable
    fun rememberSmoothFlingBehavior(): FlingBehavior {
        return ScrollableDefaults.flingBehavior()
    }
}
