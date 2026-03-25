package com.thevault.app.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionDetailsViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["id"])

    private val _state = MutableStateFlow(SubscriptionDetailsState(isLoading = true))
    val state: StateFlow<SubscriptionDetailsState> = _state.asStateFlow()

    init {
        loadSubscription()
    }

    private fun loadSubscription() {
        viewModelScope.launch {
            repository.getAllSubscriptions()
                .map { list -> list.find { it.id == id } }
                .collect { sub ->
                    _state.update { it.copy(subscription = sub, isLoading = false) }
                }
        }
    }

    fun deleteSubscription() {
        viewModelScope.launch {
            repository.deleteSubscription(id)
        }
    }
}

data class SubscriptionDetailsState(
    val subscription: Subscription? = null,
    val isLoading: Boolean = false
)
