package com.sharjeel.newsapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sharjeel.newsapp.ui.screens.MainScreen
import com.sharjeel.newsapp.ui.screens.auth.ForgotPasswordNavigation
import com.sharjeel.newsapp.ui.screens.auth.LoginScreen
import com.sharjeel.newsapp.ui.screens.auth.SignupScreen
import com.sharjeel.newsapp.ui.screens.author_profile.AuthorProfileScreen
import com.sharjeel.newsapp.ui.screens.details.CommentScreen
import com.sharjeel.newsapp.ui.screens.details.CreateNewsScreen
import com.sharjeel.newsapp.ui.screens.details.DetailScreen
import com.sharjeel.newsapp.ui.screens.details.PublishedNewsDetailScreen
import com.sharjeel.newsapp.ui.screens.latest_news.LatestNewsScreen
import com.sharjeel.newsapp.ui.screens.notification.NotificationScreen
import com.sharjeel.newsapp.ui.screens.onboarding.AdvancedSplashScreen
import com.sharjeel.newsapp.ui.screens.onboarding.FillProfileScreen
import com.sharjeel.newsapp.ui.screens.onboarding.OnboardingScreen
import com.sharjeel.newsapp.ui.screens.onboarding.OnboardingViewModel
import com.sharjeel.newsapp.ui.screens.onboarding.SelectCountryScreen
import com.sharjeel.newsapp.ui.screens.onboarding.SelectSourcesScreen
import com.sharjeel.newsapp.ui.screens.onboarding.SelectTopicsScreen
import com.sharjeel.newsapp.ui.screens.profile.EditProfileScreen
import com.sharjeel.newsapp.ui.screens.search.SearchScreen
import com.sharjeel.newsapp.ui.screens.settings.SettingsScreen
import com.sharjeel.newsapp.ui.screens.trending.TrendingScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object SelectCountry : Screen("select_country")
    data object SelectTopics : Screen("select_topics")
    data object SelectSources : Screen("select_sources")
    data object FillProfile : Screen("fill_profile")
    data object Main : Screen("main")
    data object ForgotPassword : Screen("forgot_password")
    data object Trending : Screen("trending")
    data object Notification : Screen("notification")
    data object LatestNews : Screen("latest_news")
    data object Search : Screen("search")
    data object AuthorProfile : Screen("author_profile")
    data object Settings : Screen("settings")
    data object EditProfile : Screen("edit_profile")
    data object NewsDetail : Screen("news_detail")
    data object Comments : Screen("comments")
    data object CreateNews : Screen("create_news")
    data object PublishedNewsDetail : Screen("published_news_detail")
}

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
            // Humne aapki upgraded dynamic splash screen yahan attach kar di hai
            AdvancedSplashScreen(
                onAnimationFinished = {
                    navController.navigate(startDestination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
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
                },
                onNewsItemClick = {
                    navController.navigate(Screen.NewsDetail.route)
                },
                onCreateNewsClick = {
                    navController.navigate(Screen.CreateNews.route)
                }
            )
        }
        composable(Screen.NewsDetail.route) {
            DetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCommentClick = {
                    navController.navigate(Screen.Comments.route)
                }
            )
        }
        composable(Screen.Comments.route) {
            CommentScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.CreateNews.route) {
            CreateNewsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPublishClick = {
                    navController.navigate(Screen.PublishedNewsDetail.route)
                }
            )
        }
        composable(Screen.PublishedNewsDetail.route) {
            PublishedNewsDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Trending.route) {
            TrendingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNewsItemClick = {
                    navController.navigate(Screen.NewsDetail.route)
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
                },
                onNewsItemClick = {
                    navController.navigate(Screen.NewsDetail.route)
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
                },
                onNewsItemClick = {
                    navController.navigate(Screen.NewsDetail.route)
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