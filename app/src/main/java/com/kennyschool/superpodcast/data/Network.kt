package com.kennyschool.superpodcast.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * One place to set up Retrofit for the whole app.
 *
 * This keeps the project organized:
 * - If I ever change baseUrl or JSON converter, I change it once here.
 */
object Network {

    // Moshi is the JSON parser. KotlinJsonAdapterFactory helps Moshi understand Kotlin data classes.
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Retrofit does the HTTP work + turns JSON into our data classes.
     * baseUrl MUST end with a slash (Retrofit requirement).
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/") // iTunes API base
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // This is the actual API object we'll call from our repository/viewmodel.
    val itunesApi: ITunesApi = retrofit.create(ITunesApi::class.java)
}