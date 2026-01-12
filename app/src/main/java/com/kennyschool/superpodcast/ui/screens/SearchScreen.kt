package com.kennyschool.superpodcast.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kennyschool.superpodcast.ui.viewmodel.SearchViewModel

/**
 * Search screen:
 * - user types a search term
 * - press Search
 * - show podcasts from iTunes
 *
 * Keeping UI simple so it's reliable for the assignment demo.
 */
@Composable
fun SearchScreen(
    onOpenPodcast: (feedUrl: String, artworkUrl: String, title: String) -> Unit,
    onOpenSubscriptions: () -> Unit,
    vm: SearchViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SuperPodcast",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = vm::onQueryChange,
            label = { Text("Search podcasts") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { vm.search() },
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Simple feedback while it loads
            Text(if (state.loading) "Searching..." else "Search")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onOpenSubscriptions,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Subscriptions")
        }

        state.error?.let { msg ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        // Results list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.results) { podcast ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable {
                            onOpenPodcast(podcast.feedUrl, podcast.artworkUrl, podcast.title)
                        }
                ) {
                    Row(Modifier.padding(12.dp)) {
                        // Artwork (if it fails to load, it just shows blank—fine for an assignment)
                        AsyncImage(
                            model = podcast.artworkUrl,
                            contentDescription = "Podcast artwork",
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                text = podcast.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = podcast.author,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}