package com.kennyschool.superpodcast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kennyschool.superpodcast.ui.AppRoot
import com.kennyschool.superpodcast.ui.theme.SuperPodcastTheme

/**
 * MainActivity is just the app entry point.
 * I keep it clean: theme + AppRoot navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SuperPodcastTheme {
                AppRoot()
            }
        }
    }
}