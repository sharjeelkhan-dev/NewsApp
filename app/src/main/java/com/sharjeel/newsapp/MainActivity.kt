package com.sharjeel.newsapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import android.content.pm.PackageManager
import com.sharjeel.newsapp.domain.repository.AuthRepository
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

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        printHashKey()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 1. Force Clear System Windows Flags
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        // 2. Clear Notch/Cutout Reserve Margins
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            val isOnboardingFinished = dataStoreManager.isOnboardingFinished.collectAsState(initial = null)

            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isOnboardingFinished.value != null) {
                        val startDestination = if (isOnboardingFinished.value == true) {
                            if (authRepository.isUserLoggedIn()) {
                                Screen.Main.route
                            } else {
                                Screen.Login.route
                            }
                        } else {
                            Screen.Onboarding.route
                        }
                        NavGraph(startDestination = startDestination)
                    }
                }
            }
        }
    }

    private fun printHashKey() {
        try {
            val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.signingInfo?.signingCertificateHistory
            } else {
                info.signatures
            }

            signatures?.forEach { signature ->
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("FACEBOOK_HASH", "Sahi Hash Key Ye Hai: $hashKey")
            }
        } catch (e: Exception) {
            Log.e("FACEBOOK_HASH", "Error: ${e.message}")
        }
    }
}
