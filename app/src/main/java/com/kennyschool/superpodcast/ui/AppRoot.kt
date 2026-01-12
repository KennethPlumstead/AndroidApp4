package com.kennyschool.superpodcast.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            SearchScreen(
                onOpenPodcast = { feedUrl, artworkUrl, title ->
                    navController.navigate(
                        "${Routes.DETAIL}?feedUrl=${encode(feedUrl)}&artwork=${encode(artworkUrl)}&title=${encode(title)}"
                    )
                },
                onOpenSubscriptions = {
                    navController.navigate(Routes.SUBS)
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

private fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
private fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")