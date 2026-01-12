package com.kennyschool.superpodcast.data

import com.squareup.moshi.Json

/**
 * This matches the top-level JSON object returned by the iTunes search API.
 */
data class PodcastSearchResponse(
    @Json(name = "resultCount")
    val resultCount: Int,

    @Json(name = "results")
    val results: List<PodcastDto>
)

/**
 * This maps directly to each podcast object inside "results".
 * I only included fields I actually need for the assignment.
 */
data class PodcastDto(
    @Json(name = "collectionName")
    val collectionName: String?,

    @Json(name = "artistName")
    val artistName: String?,

    @Json(name = "artworkUrl100")
    val artworkUrl100: String?,

    @Json(name = "feedUrl")
    val feedUrl: String?
)

/**
 * Clean app-level model used by the UI.
 * This keeps nullable API junk out of the UI layer.
 */
data class Podcast(
    val title: String,
    val author: String,
    val artworkUrl: String,
    val feedUrl: String
)