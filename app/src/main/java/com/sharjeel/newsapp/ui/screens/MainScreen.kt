package com.sharjeel.newsapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.sharjeel.newsapp.R
import com.sharjeel.newsapp.ui.screens.bookmark.BookmarkScreen
import com.sharjeel.newsapp.ui.screens.explore.ExploreScreen
import com.sharjeel.newsapp.ui.screens.home.HomeScreen
import com.sharjeel.newsapp.ui.screens.profile.ProfileScreen
import com.sharjeel.newsapp.ui.theme.BluePrimary
import com.sharjeel.newsapp.ui.theme.NewsAppTheme

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Home : BottomBarScreen(
        route = "home_tab",
        title = "Home",
        icon = R.drawable.home_icon_silhouette_svgrepo_com
    )
    object Explore : BottomBarScreen(
        route = "explore_tab",
        title = "Explore",
        icon = R.drawable.search_categories_icon
    )
    object Bookmark : BottomBarScreen(
        route = "bookmark_tab",
        title = "Bookmark",
        icon = R.drawable.saved_bookmark_icon
    )
    object Profile : BottomBarScreen(
        route = "profile_tab",
        title = "Profile",
        icon = R.drawable.ic_avatar
    )
}

@Composable
fun MainScreen(
    onSeeAllTrendingClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSeeAllLatestClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNewsItemClick: (String) -> Unit,
    onCreateNewsClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val user by viewModel.userState

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigation(navController = navController, profileImageUrl = user?.profileImageUrl ?: "")
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(
                    onSeeAllTrendingClick = onSeeAllTrendingClick,
                    onNotificationClick = onNotificationClick,
                    onSeeAllLatestClick = onSeeAllLatestClick,
                    onSearchClick = onSearchClick,
                    onAuthorClick = onAuthorClick,
                    onFilterClick = { /* TODO: Implement filter */ },
                    onNewsItemClick = onNewsItemClick
                )
            }
            composable(BottomBarScreen.Explore.route) {
                ExploreScreen(
                    onSearchClick = onSearchClick,
                    onAuthorClick = { sourceId -> onAuthorClick(sourceId) },
                    onNewsItemClick = { url -> onNewsItemClick(url) }
                )
            }
            composable(BottomBarScreen.Bookmark.route) {
                BookmarkScreen(
                    onNewsItemClick = onNewsItemClick
                )
            }
            composable(BottomBarScreen.Profile.route) {
                ProfileScreen(
                    onSettingsClick = onSettingsClick,
                    onEditProfileClick = onEditProfileClick,
                    onLogoutClick = onLogout,
                    onNewsItemClick = onNewsItemClick,
                    onCreateNewsClick = onCreateNewsClick
                )
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: androidx.navigation.NavHostController, profileImageUrl: String = "") {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Explore,
        BottomBarScreen.Bookmark,
        BottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold
                            else FontWeight.Normal
                        )
                    )
                },
                icon = {
                    val iconSize = 24.dp
                    if (screen is BottomBarScreen.Profile && profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = screen.title,
                            modifier = Modifier
                                .size(iconSize)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) Modifier.border(2.dp, BluePrimary, CircleShape)
                                    else Modifier
                                ),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BluePrimary,
                    selectedTextColor = BluePrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    NewsAppTheme {
        BottomNavigation(navController = rememberNavController())
    }
}
