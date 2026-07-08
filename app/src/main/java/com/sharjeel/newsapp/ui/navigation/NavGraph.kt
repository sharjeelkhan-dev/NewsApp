package com.sharjeel.newsapp.ui.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sharjeel.newsapp.ui.screens.MainScreen
import com.sharjeel.newsapp.ui.screens.auth.AuthViewModel
import com.sharjeel.newsapp.ui.screens.auth.ForgotPasswordNavigation
import com.sharjeel.newsapp.ui.screens.auth.LoginScreen
import com.sharjeel.newsapp.ui.screens.auth.SignupScreen
import com.sharjeel.newsapp.ui.screens.author_profile.AuthorProfileScreen
import com.sharjeel.newsapp.ui.screens.details.CommentScreen
import com.sharjeel.newsapp.ui.screens.details.CreateNewsScreen
import com.sharjeel.newsapp.ui.screens.details.DetailScreen
import com.sharjeel.newsapp.ui.screens.details.DetailViewModel
import com.sharjeel.newsapp.ui.screens.details.PublishedNewsDetailScreen
import com.sharjeel.newsapp.ui.screens.explore.ExploreScreen
import com.sharjeel.newsapp.ui.screens.explore.ExploreViewModel
import com.sharjeel.newsapp.ui.screens.latest_news.LatestNewsScreen
import com.sharjeel.newsapp.ui.screens.latest_news.LatestNewsViewModel
import com.sharjeel.newsapp.ui.screens.notification.NotificationScreen
import com.sharjeel.newsapp.ui.screens.onboarding.AdvancedSplashScreen
import com.sharjeel.newsapp.ui.screens.onboarding.FillProfileScreen
import com.sharjeel.newsapp.ui.screens.onboarding.OnboardingScreen
import com.sharjeel.newsapp.ui.screens.onboarding.OnboardingViewModel
import com.sharjeel.newsapp.ui.screens.onboarding.SelectCountryScreen
import com.sharjeel.newsapp.ui.screens.onboarding.SelectSourcesScreen
import com.sharjeel.newsapp.ui.screens.onboarding.SelectTopicsScreen
import com.sharjeel.newsapp.ui.screens.profile.EditProfileScreen
import com.sharjeel.newsapp.ui.screens.profile.ProfileViewModel
import com.sharjeel.newsapp.ui.screens.search.SearchScreen
import com.sharjeel.newsapp.ui.screens.settings.SettingsScreen
import com.sharjeel.newsapp.ui.screens.trending.TrendingScreen
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Signup : Screen("signup")
    data object SignupOtp : Screen("signup_otp")
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
    data object Explore : Screen("explore")
    data object AuthorProfile : Screen("author_profile/{sourceId}") {
        fun createRoute(sourceId: String) = "author_profile/$sourceId"
    }
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
    val context = LocalContext.current
    val detailViewModel: DetailViewModel = hiltViewModel()
    val homeViewModel: com.sharjeel.newsapp.ui.screens.home.HomeViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
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
            val viewModel: AuthViewModel = hiltViewModel()
            val savedEmail by viewModel.savedEmail.collectAsState(initial = "")
            val isRememberMeChecked by viewModel.isRememberMeChecked.collectAsState(initial = false)

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is AuthViewModel.UiEvent.NavigateToHome -> {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        is AuthViewModel.UiEvent.ShowError -> {
                            val message = event.message
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                        is AuthViewModel.UiEvent.ShowMessage -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }

            LoginScreen(
                onLoginClick = { email, password, rememberMe ->
                    viewModel.login(email, password, rememberMe)
                },
                onSignupClick = {
                    navController.navigate(Screen.Signup.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onGoogleSignInClick = { idToken ->
                    viewModel.signInWithGoogle(idToken)
                },
                onFacebookSignInClick = { accessToken ->
                    viewModel.signInWithFacebook(accessToken)
                },
                isLoading = viewModel.isLoading.value,
                initialEmail = savedEmail,
                initialRememberMe = isRememberMeChecked
            )
        }
        composable(Screen.ForgotPassword.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            var navigateToCongratulations by remember { mutableStateOf(false) }
            var navigateToOtp by remember { mutableStateOf<String?>(null) }
            var navigateToResetPassword by remember { mutableStateOf(false) }

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is AuthViewModel.UiEvent.NavigateToCongratulations -> {
                            navigateToCongratulations = true
                        }
                        is AuthViewModel.UiEvent.NavigateToOtp -> {
                            navigateToOtp = event.phoneNumber
                        }
                        is AuthViewModel.UiEvent.NavigateToResetPassword -> {
                            navigateToResetPassword = true
                        }
                        is AuthViewModel.UiEvent.NavigateToHome -> {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                            }
                        }
                        is AuthViewModel.UiEvent.ShowError -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                        }
                        is AuthViewModel.UiEvent.ShowMessage -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }

            ForgotPasswordNavigation(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                },
                onSendResetEmail = { email ->
                    viewModel.resetPassword(email)
                },
                onSendOtp = { phone, activity ->
                    viewModel.sendOtp(phone, activity)
                },
                onVerifyOtp = { otp ->
                    viewModel.verifyOtp(otp)
                },
                isLoading = viewModel.isLoading.value,
                navigateToCongratulations = navigateToCongratulations,
                navigateToOtp = navigateToOtp,
                navigateToResetPassword = navigateToResetPassword
            )
        }
        composable(Screen.Signup.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val activity = context as Activity

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is AuthViewModel.UiEvent.NavigateToOtp -> {
                            navController.navigate(Screen.SignupOtp.route)
                        }
                        is AuthViewModel.UiEvent.NavigateToOnboarding -> {
                            navController.navigate(Screen.SelectCountry.route)
                        }
                        is AuthViewModel.UiEvent.ShowError -> {
                            val message = event.message
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                        else -> {}
                    }
                }
            }

            SignupScreen(
                onSignupClick = { identifier, password, rememberMe ->
                    if (identifier.contains("@")) {
                        viewModel.signup(identifier, password, rememberMe)
                    } else {
                        viewModel.handlePhoneSignup(identifier, activity)
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                isLoading = viewModel.isLoading.value
            )
        }
        composable(Screen.SignupOtp.route) {
            val backStackEntry = remember(it) { navController.getBackStackEntry(Screen.Signup.route) }
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    if (event is AuthViewModel.UiEvent.NavigateToOnboarding) {
                        navController.navigate(Screen.SelectCountry.route)
                    } else if (event is AuthViewModel.UiEvent.ShowError) {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            com.sharjeel.newsapp.ui.screens.auth.OtpVerificationScreen(
                onBackClick = { navController.popBackStack() },
                onVerifyClick = { otp ->
                    viewModel.verifySignupOtp(otp)
                },
                isLoading = viewModel.isLoading.value,
                phoneNumber = viewModel.signupUser.phoneNumber
            )
        }
        composable(Screen.SelectCountry.route) {
            val backStackEntry = remember(it) { navController.getBackStackEntry(Screen.Signup.route) }
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)
            SelectCountryScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { country ->
                    viewModel.updateSignupData(country = country)
                    viewModel.saveSignupProgress()
                    navController.navigate(Screen.SelectTopics.route)
                }
            )
        }
        composable(Screen.SelectTopics.route) {
            val backStackEntry = remember(it) { navController.getBackStackEntry(Screen.Signup.route) }
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)
            SelectTopicsScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { topics ->
                    viewModel.updateSignupData(topics = topics)
                    viewModel.saveSignupProgress()
                    navController.navigate(Screen.SelectSources.route)
                }
            )
        }
        composable(Screen.SelectSources.route) {
            val backStackEntry = remember(it) { navController.getBackStackEntry(Screen.Signup.route) }
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)
            SelectSourcesScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { sources ->
                    viewModel.updateSignupData(sources = sources)
                    viewModel.saveSignupProgress()
                    navController.navigate(Screen.FillProfile.route)
                }
            )
        }
        composable(Screen.FillProfile.route) {
            val backStackEntry = remember(it) { navController.getBackStackEntry(Screen.Signup.route) }
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)

            LaunchedEffect(key1 = true) {
                viewModel.eventFlow.collectLatest { event ->
                    if (event is AuthViewModel.UiEvent.NavigateToHome) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Signup.route) { inclusive = true }
                        }
                    } else if (event is AuthViewModel.UiEvent.ShowError) {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            FillProfileScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { username, fullName, email, phoneNumber, bio, website ->
                    viewModel.updateSignupData(
                        username = username,
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        bio = bio,
                        website = website
                    )
                    viewModel.completeSignup()
                },
                onImagePick = { uri ->
                    viewModel.uploadProfileImage(uri)
                },
                initialEmail = viewModel.signupUser.email,
                initialPhone = viewModel.signupUser.phoneNumber,
                profileImageUrl = viewModel.signupUser.profileImageUrl,
                isLoading = viewModel.isLoading.value
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
                onAuthorClick = { sourceId: String ->
                    navController.navigate(Screen.AuthorProfile.createRoute(sourceId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onNewsItemClick = { url: String ->
                    val foundArticle = homeViewModel.trendingNews.value.find { it.url == url }
                        ?: homeViewModel.latestNews.value.find { it.url == url }

                    if (foundArticle != null) {
                        detailViewModel.setArticle(foundArticle)
                        navController.navigate(Screen.NewsDetail.route)
                    }
                },
                onCreateNewsClick = {
                    navController.navigate(Screen.CreateNews.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Explore.route) {
            val exploreViewModel: ExploreViewModel = hiltViewModel()
            ExploreScreen(
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onAuthorClick = { sourceId: String ->
                    navController.navigate(Screen.AuthorProfile.createRoute(sourceId))
                },
                onNewsItemClick = { url: String ->
                    val foundArticle = exploreViewModel.popularNews.value.find { it.url == url }
                    if (foundArticle != null) {
                        detailViewModel.setArticle(foundArticle)
                        navController.navigate(Screen.NewsDetail.route)
                    }
                },
                viewModel = exploreViewModel
            )
        }
        composable(Screen.NewsDetail.route) {
            val article = detailViewModel.article.value
            DetailScreen(
                article = article,
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
            val trendingViewModel: com.sharjeel.newsapp.ui.screens.trending.TrendingViewModel = hiltViewModel()
            TrendingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNewsItemClick = { url: String ->
                    val foundArticle = trendingViewModel.trendingNews.value.find { it.url == url }
                    if (foundArticle != null) {
                        detailViewModel.setArticle(foundArticle)
                        navController.navigate(Screen.NewsDetail.route)
                    }
                },
                viewModel = trendingViewModel
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
            val latestNewsViewModel: LatestNewsViewModel = hiltViewModel()
            LatestNewsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAuthorClick = { sourceId: String ->
                    navController.navigate(Screen.AuthorProfile.createRoute(sourceId))
                },
                onNewsItemClick = { url: String ->
                    val foundArticle = latestNewsViewModel.latestNews.value.find { it.url == url }
                    if (foundArticle != null) {
                        detailViewModel.setArticle(foundArticle)
                        navController.navigate(Screen.NewsDetail.route)
                    }
                },
                viewModel = latestNewsViewModel
            )
        }
        composable(Screen.Search.route) {
            val searchViewModel: com.sharjeel.newsapp.ui.screens.search.SearchViewModel = hiltViewModel()
            SearchScreen(
                onAuthorClick = { sourceId: String ->
                    navController.navigate(Screen.AuthorProfile.createRoute(sourceId))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNewsItemClick = { url: String ->
                    val foundArticle = searchViewModel.searchResults.value.find { it.url == url }
                    if (foundArticle != null) {
                        detailViewModel.setArticle(foundArticle)
                        navController.navigate(Screen.NewsDetail.route)
                    }
                },
                viewModel = searchViewModel
            )
        }
        composable(
            route = Screen.AuthorProfile.route,
            arguments = listOf(
                androidx.navigation.navArgument("sourceId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val sourceId = backStackEntry.arguments?.getString("sourceId") ?: ""
            AuthorProfileScreen(
                sourceId = sourceId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Settings.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}