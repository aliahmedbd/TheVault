package com.thevault.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.NotificationRepository
import com.thevault.app.domain.DeleteSubscriptionUseCase
import com.thevault.app.domain.GetSubscriptionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
    private val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadSubscriptions()
        observeUnreadNotifications()
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getSubscriptionsUseCase().collect { subs ->
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
    }

    private fun observeUnreadNotifications() {
        notificationRepository.getUnreadCount()
            .onEach { count ->
                _state.update { it.copy(unreadNotificationCount = count) }
            }
            .launchIn(viewModelScope)
    }

    fun deleteSubscription(id: String) {
        viewModelScope.launch {
            deleteSubscriptionUseCase(id)
        }
    }
}
