package com.thevault.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.Subscription
import com.thevault.app.domain.GetSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsListViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _state = MutableStateFlow(SubscriptionsListState())
    val state: StateFlow<SubscriptionsListState> = _state.asStateFlow()

    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        combine(
            getSubscriptionsUseCase(),
            _searchQuery
        ) { subs, query ->
            if (query.isBlank()) {
                subs
            } else {
                subs.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .onStart { _state.update { it.copy(isLoading = true) } }
        .onEach { filteredSubs ->
            _state.update {
                it.copy(
                    subscriptions = filteredSubs,
                    isLoading = false
                )
            }
        }
        .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}

data class SubscriptionsListState(
    val subscriptions: List<Subscription> = emptyList(),
    val isLoading: Boolean = false
)
