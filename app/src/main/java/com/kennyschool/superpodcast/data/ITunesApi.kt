package com.kennyschool.superpodcast.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * iTunes Search API (podcasts).
 *
 * I'm keeping this dead simple:
 * - term = what the user typed
 * - media=podcast makes sure we don't get music/movies/etc.
 *
 * Docs are public, and this endpoint is stable enough for class projects.
 */
interface ITunesApi {

    /**
     * Example call:
     * https://itunes.apple.com/search?media=podcast&term=science&limit=25
     *
     * suspend = runs nicely with coroutines (so we don't block the UI thread).
     */
    @GET("search")
    suspend fun searchPodcasts(
        @Query("media") media: String = "podcast",
        @Query("term") term: String,
        @Query("limit") limit: Int = 25
    ): PodcastSearchResponse
}