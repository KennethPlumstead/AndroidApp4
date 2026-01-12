package com.kennyschool.superpodcast.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * What I'm saving locally:
 * - feedUrl = unique ID for the podcast (this is the important part)
 * - title/artworkUrl = just so the UI can show what I saved
 */
@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey val feedUrl: String,
    val title: String,
    val artworkUrl: String
)