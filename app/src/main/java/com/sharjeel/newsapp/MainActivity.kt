package com.sharjeel.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.sharjeel.newsapp.ui.navigation.NavGraph
import com.sharjeel.newsapp.ui.navigation.Screen
import com.sharjeel.newsapp.ui.theme.NewsAppTheme
import com.sharjeel.newsapp.util.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isOnboardingFinished = dataStoreManager.isOnboardingFinished.collectAsState(initial = null)
            
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isOnboardingFinished.value != null) {
                        val startDestination = if (isOnboardingFinished.value == true) {
                            Screen.Login.route
                        } else {
                            Screen.Onboarding.route
                        }
                        NavGraph(startDestination = startDestination)
                    }
                }
            }
        }
    }
}
