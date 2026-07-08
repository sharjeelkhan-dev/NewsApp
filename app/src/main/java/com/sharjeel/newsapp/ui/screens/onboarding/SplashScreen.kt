package com.sharjeel.newsapp.ui.screens.onboarding

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun AdvancedSplashScreen(
    onAnimationFinished: () -> Unit
) {
    var currentPhase by remember { mutableIntStateOf(0) }

    val iconScale by animateFloatAsState(
        targetValue = when (currentPhase) {
            0 -> 0.0f
            1 -> 2.2f
            else -> 1.0f
        },
        animationSpec = when (currentPhase) {
            1 -> tween(durationMillis = 500, easing = EaseOutBack)
            else -> spring(
                dampingRatio = 0.55f,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        label = "CinematicIconScale"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (currentPhase >= 2) 1.0f else 0.0f,
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "TextRevealAlpha"
    )

    val splitTextOffset by animateDpAsState(
        targetValue = if (currentPhase >= 2) 0.dp else 30.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "SpellingSplitOffset"
    )

    // Cinematic Timing Engine
    LaunchedEffect(Unit) {
        delay(200.milliseconds)
        currentPhase = 1
        delay(600.milliseconds)
        currentPhase = 2
        delay(2000.milliseconds)
        onAnimationFinished()
    }

    val appBackground = MaterialTheme.colorScheme.background
    val appPrimaryBlue = MaterialTheme.colorScheme.primary
    val appOnSurface = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.wrapContentSize()
            ) {

                Text(
                    text = "Akh",
                    modifier = Modifier
                        .offset(x = -splitTextOffset)
                        .alpha(textAlpha),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        color = appOnSurface
                    )
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .scale(iconScale)
                        .alpha(if (currentPhase > 0) 1f else 0f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.television_icon),
                        contentDescription = "App Logo Identity",
                        tint = appPrimaryBlue,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Text(
                    text = "bar",
                    modifier = Modifier
                        .offset(x = splitTextOffset)
                        .alpha(textAlpha),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        color = appOnSurface
                    )
                )
            }

            Text(
                text = "TRUTH IN FOCUS",
                modifier = Modifier.alpha(textAlpha),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 5.sp,
                    color = appOnSurface.copy(alpha = 0.45f)
                )
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
fun AdvancedSplashScreenPreview() {
    NewsAppTheme {
        AdvancedSplashScreen(onAnimationFinished = {})
    }
}