package com.kennyschool.superpodcast.repo

import android.content.Context
import com.kennyschool.superpodcast.data.Episode
import com.kennyschool.superpodcast.data.Network
import com.kennyschool.superpodcast.data.Podcast
import com.kennyschool.superpodcast.data.RssDownloader
import com.kennyschool.superpodcast.data.RssParser
import com.kennyschool.superpodcast.db.AppDatabase
import com.kennyschool.superpodcast.db.SubscriptionEntity

/**
 * Repository = the "middle man" between UI/ViewModels and the data layer.
 *
 * I’m keeping it simple:
 * - iTunes search (Retrofit)
 * - RSS episodes (OkHttp + XmlPullParser)
 * - subscriptions (Room)
 */
class PodcastRepository(context: Context) {

    // Room DAO for subscriptions
    private val subscriptionDao = AppDatabase.get(context).subscriptionDao()

    // --- SEARCH ---

    suspend fun search(term: String): List<Podcast> {
        if (term.isBlank()) return emptyList()

        val response = Network.itunesApi.searchPodcasts(term = term.trim())

        return response.results
            .filter { !it.feedUrl.isNullOrBlank() }
            .map { dto ->
                Podcast(
                    title = dto.collectionName ?: "(No title)",
                    author = dto.artistName ?: "(Unknown author)",
                    artworkUrl = dto.artworkUrl100 ?: "",
                    feedUrl = dto.feedUrl ?: ""
                )
            }
    }

    // --- EPISODES ---

    suspend fun loadEpisodes(feedUrl: String): List<Episode> {
        if (feedUrl.isBlank()) return emptyList()

        val xml = RssDownloader.download(feedUrl)
        if (xml.isBlank()) return emptyList()

        return RssParser.parseEpisodes(xml)
    }

    // --- SUBSCRIPTIONS ---

    suspend fun isSubscribed(feedUrl: String): Boolean {
        return subscriptionDao.countByFeedUrl(feedUrl) > 0
    }

    suspend fun subscribe(feedUrl: String, title: String, artworkUrl: String) {
        subscriptionDao.upsert(
            SubscriptionEntity(
                feedUrl = feedUrl,
                title = title,
                artworkUrl = artworkUrl
            )
        )
    }

    suspend fun unsubscribe(feedUrl: String) {
        subscriptionDao.delete(feedUrl)
    }

    /**
     * Get all subscribed podcasts.
     * Used by the Subscriptions screen.
     */
    suspend fun getSubscriptions(): List<SubscriptionEntity> {
        return subscriptionDao.getAll()
    }
}