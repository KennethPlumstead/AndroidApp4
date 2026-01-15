package com.kennyschool.superpodcast.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.kennyschool.superpodcast.ui.screens.PlayerScreen
import com.kennyschool.superpodcast.ui.screens.PodcastDetailScreen
import com.kennyschool.superpodcast.ui.screens.SearchScreen
import com.kennyschool.superpodcast.ui.screens.SubscriptionsScreen
import java.net.URLDecoder
import java.net.URLEncoder

private object Routes {
    const val SEARCH = "search"
    const val DETAIL = "detail"
    const val PLAYER = "player"
    const val SUBS = "subs"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Show bottom nav ONLY on the two main screens
    val showBottomBar = currentDestination?.route in setOf(Routes.SEARCH, Routes.SUBS)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == Routes.SEARCH } == true,
                        onClick = {
                            navController.navigate(Routes.SEARCH) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                        label = { Text("Search") }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == Routes.SUBS } == true,
                        onClick = {
                            navController.navigate(Routes.SUBS) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Star, contentDescription = "Subscriptions") },
                        label = { Text("Subs") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SEARCH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.SEARCH) {
                SearchScreen(
                    onOpenPodcast = { feedUrl, artworkUrl, title ->
                        navController.navigate(
                            "${Routes.DETAIL}?feedUrl=${encode(feedUrl)}&artwork=${encode(artworkUrl)}&title=${encode(title)}"
                        )
                    },
                    onOpenSubscriptions = {
                        navController.navigate(Routes.SUBS) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.SUBS) {
                SubscriptionsScreen(
                    onOpenPodcast = { feedUrl, artworkUrl, title ->
                        navController.navigate(
                            "${Routes.DETAIL}?feedUrl=${encode(feedUrl)}&artwork=${encode(artworkUrl)}&title=${encode(title)}"
                        )
                    },
                    // With bottom nav, the back button is optional,
                    // but keeping it is fine for your assignment.
                    onBack = { navController.popBackStack() }
                )
            }

            composable("${Routes.DETAIL}?feedUrl={feedUrl}&artwork={artwork}&title={title}") { backStack ->
                val feedUrl = decode(backStack.arguments?.getString("feedUrl").orEmpty())
                val artworkUrl = decode(backStack.arguments?.getString("artwork").orEmpty())
                val title = decode(backStack.arguments?.getString("title").orEmpty())

                PodcastDetailScreen(
                    feedUrl = feedUrl,
                    artworkUrl = artworkUrl,
                    title = title,
                    onBack = { navController.popBackStack() },
                    onPlay = { audioUrl, episodeTitle ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("audioUrl", audioUrl)
                        navController.currentBackStackEntry?.savedStateHandle?.set("episodeTitle", episodeTitle)
                        navController.navigate(Routes.PLAYER)
                    }
                )
            }

            composable(Routes.PLAYER) {
                val audioUrl =
                    navController.previousBackStackEntry?.savedStateHandle?.get<String>("audioUrl").orEmpty()
                val episodeTitle =
                    navController.previousBackStackEntry?.savedStateHandle?.get<String>("episodeTitle").orEmpty()

                PlayerScreen(
                    audioUrl = audioUrl,
                    episodeTitle = episodeTitle,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
private fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")