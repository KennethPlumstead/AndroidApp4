package com.kennyschool.superpodcast.data

/**
 * Keeping it simple on purpose:
 * - title = what we show in the list
 * - audioUrl = what we send to the player
 */
data class Episode(
    val title: String,
    val audioUrl: String
)