package com.kennyschool.superpodcast.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kennyschool.superpodcast.data.Episode
import com.kennyschool.superpodcast.repo.PodcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * DetailViewModel:
 * - loads episodes from RSS
 * - tracks subscription status using Room
 * - exposes everything as simple state for Compose
 */
data class DetailUiState(
    val loading: Boolean = false,
    val episodes: List<Episode> = emptyList(),
    val subscribed: Boolean = false,
    val error: String? = null
)

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PodcastRepository(app.applicationContext)

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state

    fun load(feedUrl: String) {
        if (feedUrl.isBlank()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)

            try {
                val subscribed = repo.isSubscribed(feedUrl)
                val episodes = repo.loadEpisodes(feedUrl)

                _state.value = _state.value.copy(
                    loading = false,
                    subscribed = subscribed,
                    episodes = episodes
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Failed to load episodes"
                )
            }
        }
    }

    fun toggleSubscription(feedUrl: String, title: String, artworkUrl: String) {
        if (feedUrl.isBlank()) return

        viewModelScope.launch {
            try {
                if (_state.value.subscribed) {
                    repo.unsubscribe(feedUrl)
                    _state.value = _state.value.copy(subscribed = false)
                } else {
                    repo.subscribe(feedUrl, title, artworkUrl)
                    _state.value = _state.value.copy(subscribed = true)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Subscription failed")
            }
        }
    }
}