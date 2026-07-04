package com.sharjeel.newsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sharjeel.newsapp.ui.screens.MainScreen
import com.sharjeel.newsapp.ui.screens.auth.ForgotPasswordNavigation
import com.sharjeel.newsapp.ui.screens.auth.LoginScreen
import com.sharjeel.newsapp.ui.screens.auth.SignupScreen
import com.sharjeel.newsapp.ui.screens.onboarding.*
import com.sharjeel.newsapp.ui.screens.trending.TrendingScreen
import com.sharjeel.newsapp.ui.screens.notification.NotificationScreen
import com.sharjeel.newsapp.ui.screens.latest_news.LatestNewsScreen
import com.sharjeel.newsapp.ui.screens.search.SearchScreen
import com.sharjeel.newsapp.ui.screens.author_profile.AuthorProfileScreen
import com.sharjeel.newsapp.ui.screens.settings.SettingsScreen
import com.sharjeel.newsapp.ui.screens.profile.EditProfileScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object SelectCountry : Screen("select_country")
    object SelectTopics : Screen("select_topics")
    object SelectSources : Screen("select_sources")
    object FillProfile : Screen("fill_profile")
    object Main : Screen("main")
    object ForgotPassword : Screen("forgot_password")
    object Trending : Screen("trending")
    object Notification : Screen("notification")
    object LatestNews : Screen("latest_news")
    object Search : Screen("search")
    object AuthorProfile : Screen("author_profile")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
}

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onFinished = {
                    viewModel.saveOnboardingFinished()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Screen.Signup.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordNavigation(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupClick = { _, _ ->
                    navController.navigate(Screen.SelectCountry.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.SelectCountry.route) {
            SelectCountryScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { _ ->
                    navController.navigate(Screen.SelectTopics.route)
                }
            )
        }
        composable(Screen.SelectTopics.route) {
            SelectTopicsScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { _ ->
                    navController.navigate(Screen.SelectSources.route)
                }
            )
        }
        composable(Screen.SelectSources.route) {
            SelectSourcesScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { _ ->
                    navController.navigate(Screen.FillProfile.route)
                }
            )
        }
        composable(Screen.FillProfile.route) {
            FillProfileScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { _, _, _, _ ->
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                onSeeAllTrendingClick = {
                    navController.navigate(Screen.Trending.route)
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notification.route)
                },
                onSeeAllLatestClick = {
                    navController.navigate(Screen.LatestNews.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onAuthorClick = {
                    navController.navigate(Screen.AuthorProfile.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                }
            )
        }
        composable(Screen.Trending.route) {
            TrendingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Notification.route) {
            NotificationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.LatestNews.route) {
            LatestNewsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAuthorClick = {
                    navController.navigate(Screen.AuthorProfile.route)
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen(
                onAuthorClick = {
                    navController.navigate(Screen.AuthorProfile.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.AuthorProfile.route) {
            AuthorProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
