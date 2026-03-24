package com.thevault.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.INITIAL_SUBSCRIPTIONS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect: SharedFlow<DashboardEffect> = _effect.asSharedFlow()

    init {
        handleIntent(DashboardIntent.LoadSubscriptions)
    }

    fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadSubscriptions -> loadSubscriptions()
            is DashboardIntent.DeleteSubscription -> deleteSubscription(intent.id)
        }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Simulate network delay
            // delay(1000)
            val subs = INITIAL_SUBSCRIPTIONS
            val total = subs.sumOf { if (it.billingCycle == "Monthly") it.price else it.price / 12 }
            _state.update { 
                it.copy(
                    subscriptions = subs,
                    totalMonthlySpend = total,
                    savedThisMonth = 24.0,
                    isLoading = false
                )
            }
        }
    }

    private fun deleteSubscription(id: String) {
        _state.update { currentState ->
            val updatedList = currentState.subscriptions.filter { it.id != id }
            val total = updatedList.sumOf { if (it.billingCycle == "Monthly") it.price else it.price / 12 }
            currentState.copy(
                subscriptions = updatedList,
                totalMonthlySpend = total
            )
        }
    }
}
