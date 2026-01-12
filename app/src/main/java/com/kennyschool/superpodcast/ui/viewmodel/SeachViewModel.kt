package com.kennyschool.superpodcast.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kennyschool.superpodcast.data.Podcast
import com.kennyschool.superpodcast.repo.PodcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * SearchViewModel:
 * - holds the search query
 * - calls the repo
 * - exposes results + loading + errors for the UI
 *
 * I'm using StateFlow because it plays nicely with Compose.
 */
data class SearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val results: List<Podcast> = emptyList(),
    val error: String? = null
)

class SearchViewModel(app: Application) : AndroidViewModel(app) {

    // Repository needs a Context because of Room
    private val repo = PodcastRepository(app.applicationContext)

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state

    fun onQueryChange(newValue: String) {
        _state.value = _state.value.copy(query = newValue)
    }

    fun search() {
        val term = _state.value.query.trim()

        // Simple guard: no blank searches
        if (term.isBlank()) {
            _state.value = _state.value.copy(error = "Type something first 🙂")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)

            try {
                val results = repo.search(term)
                _state.value = _state.value.copy(loading = false, results = results)
            } catch (e: Exception) {
                // Not overthinking it: show the message so I can debug if needed.
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }
}