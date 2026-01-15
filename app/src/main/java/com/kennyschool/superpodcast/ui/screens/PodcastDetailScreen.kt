package com.kennyschool.superpodcast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kennyschool.superpodcast.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailScreen(
    feedUrl: String,
    artworkUrl: String,
    title: String,
    onBack: () -> Unit,
    onPlay: (audioUrl: String, episodeTitle: String) -> Unit,
    vm: DetailViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(feedUrl) {
        vm.load(feedUrl)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {
            Row {
                AsyncImage(
                    model = artworkUrl,
                    contentDescription = "Podcast artwork",
                    modifier = Modifier.size(90.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { vm.toggleSubscription(feedUrl, title, artworkUrl) }
                    ) {
                        Text(if (state.subscribed) "Unsubscribe" else "Subscribe")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            state.error?.let { msg ->
                Text(text = msg, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            if (state.loading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
                return@Column
            }

            Text(
                text = "Episodes",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.episodes) { ep ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                onPlay(ep.audioUrl, ep.title)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                text = ep.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}