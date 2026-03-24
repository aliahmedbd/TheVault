package com.thevault.app.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.domain.GetSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsListViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionsListState())
    val state: StateFlow<SubscriptionsListState> = _state.asStateFlow()

    init {
        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getSubscriptionsUseCase().collect { subs ->
                _state.update {
                    it.copy(
                        subscriptions = subs,
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class SubscriptionsListState(
    val subscriptions: List<com.thevault.app.data.Subscription> = emptyList(),
    val isLoading: Boolean = false
)
