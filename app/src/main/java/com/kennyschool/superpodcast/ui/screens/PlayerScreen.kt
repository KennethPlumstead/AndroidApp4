package com.kennyschool.superpodcast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    audioUrl: String,
    episodeTitle: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var isPlaying by remember { mutableStateOf(false) }

    // Tracks if the user *wants* audio playing right now (helps with buffering text)
    var userWantsPlay by remember { mutableStateOf(false) }

    var status by remember { mutableStateOf("Ready") }

    val player = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(audioUrl) {
        if (audioUrl.isBlank()) {
            status = "No audio link found for this episode."
            return@LaunchedEffect
        }

        // Load the audio, but don't auto-play.
        status = "Ready"
        userWantsPlay = false
        isPlaying = false

        player.setMediaItem(MediaItem.fromUri(audioUrl))
        player.prepare()
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing

                status = when {
                    playing -> "Playing"
                    userWantsPlay -> "Buffering..."
                    else -> "Paused"
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                status = when (state) {
                    Player.STATE_IDLE -> "Idle"
                    Player.STATE_BUFFERING -> if (userWantsPlay) "Buffering..." else "Ready"
                    Player.STATE_READY -> if (isPlaying) "Playing" else if (userWantsPlay) "Ready" else "Paused"
                    Player.STATE_ENDED -> {
                        userWantsPlay = false
                        "Finished"
                    }
                    else -> status
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                userWantsPlay = false
                status = "Playback error: ${error.message ?: "Unknown"}"
            }
        }

        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // small safety cleanup so audio doesn’t keep playing after leaving
                            player.pause()
                            userWantsPlay = false
                            onBack()
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            Text(text = "Now Playing", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(text = episodeTitle, style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    if (audioUrl.isBlank()) return@Button

                    if (isPlaying) {
                        player.pause()
                        userWantsPlay = false
                        status = "Paused"
                    } else {
                        userWantsPlay = true
                        status = "Buffering..."
                        player.play()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = audioUrl.isNotBlank()
            ) {
                Text(if (isPlaying) "Pause" else "Play")
            }

            Spacer(Modifier.height(14.dp))

            // Status looks nicer as a “card” instead of plain text
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}