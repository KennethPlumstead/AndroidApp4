package com.kennyschool.superpodcast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main Room database for the app.
 * Version 1 is fine since this is a class assignment and we aren't doing migrations.
 */
@Database(
    entities = [SubscriptionEntity::class],
    version = 1,
    exportSchema = false // keeps the project clean (no schema folder required)
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        // Singleton so we don't accidentally create multiple DB instances.
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "superpodcast.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}