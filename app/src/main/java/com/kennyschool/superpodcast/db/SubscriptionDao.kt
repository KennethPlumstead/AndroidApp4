package com.kennyschool.superpodcast.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO = database access methods.
 * I only included what I need for Assignment 7:
 * - save a subscription
 * - delete it
 * - check if one exists
 */
@Dao
interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subscription: SubscriptionEntity)

    @Query("DELETE FROM subscriptions WHERE feedUrl = :feedUrl")
    suspend fun delete(feedUrl: String)

    // Room doesn't have a boolean return type for this style, so I return a count.
    @Query("SELECT COUNT(*) FROM subscriptions WHERE feedUrl = :feedUrl")
    suspend fun countByFeedUrl(feedUrl: String): Int

    @Query("SELECT * FROM subscriptions ORDER BY title ASC")
    suspend fun getAll(): List<SubscriptionEntity>
}