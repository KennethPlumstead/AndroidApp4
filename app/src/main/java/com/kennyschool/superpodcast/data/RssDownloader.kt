package com.kennyschool.superpodcast.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Downloads the RSS feed XML as a plain String.
 *
 * I’m using OkHttp directly here because:
 * - RSS is XML (not JSON)
 * - I don’t need a full Retrofit setup for this part
 * - It stays simple and reliable for a class project
 */
object RssDownloader {

    private val client = OkHttpClient()

    suspend fun download(feedUrl: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(feedUrl)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    // Keeping the error message readable for debugging.
                    throw IllegalStateException("RSS download failed (HTTP ${response.code})")
                }

                // If body is null for some reason, return empty string (parser will just find nothing).
                response.body?.string().orEmpty()
            }
        }
    }
}