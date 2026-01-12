package com.kennyschool.superpodcast.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kennyschool.superpodcast.db.SubscriptionEntity
import com.kennyschool.superpodcast.repo.PodcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SubscriptionsUiState(
    val loading: Boolean = false,
    val items: List<SubscriptionEntity> = emptyList(),
    val error: String? = null
)

class SubscriptionsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PodcastRepository(app.applicationContext)

    private val _state = MutableStateFlow(SubscriptionsUiState())
    val state: StateFlow<SubscriptionsUiState> = _state

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val subs = repo.getSubscriptions()
                _state.value = _state.value.copy(loading = false, items = subs)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Failed to load subscriptions"
                )
            }
        }
    }
}