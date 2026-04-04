package com.thevault.app.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.PopularSubscription
import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import com.thevault.app.domain.GetPopularSubscriptionsUseCase
import com.thevault.app.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val getPopularSubscriptionsUseCase: GetPopularSubscriptionsUseCase,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subscriptionId: String? = savedStateHandle["id"]

    private val _uiState = MutableStateFlow(AddSubscriptionState())
    val uiState: StateFlow<AddSubscriptionState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AddSubscriptionEvent>()
    val event = _event.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                loadPopularSubscriptions(query)
            }
            .launchIn(viewModelScope)

        if (subscriptionId != null) {
            loadSubscription(subscriptionId)
        }
    }

    private fun loadPopularSubscriptions(query: String = "") {
        val popular = getPopularSubscriptionsUseCase(query)
        _uiState.update { it.copy(popularSubscriptions = popular) }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(popularSearchQuery = query) }
    }

    private fun loadSubscription(id: String) {
        viewModelScope.launch {
            repository.getSubscriptionById(id)?.let { sub ->
                _uiState.update {
                    it.copy(
                        name = sub.name,
                        price = sub.price.toString(),
                        renewalDate = sub.nextBillingDate,
                        manageUrl = sub.manageUrl ?: "",
                        icon = sub.icon,
                        logoUrl = sub.logoUrl,
                        isEditing = true
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onPriceChange(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun onRenewalDateChange(date: String) {
        _uiState.update { it.copy(renewalDate = date) }
    }

    fun onManageUrlChange(url: String) {
        _uiState.update { it.copy(manageUrl = url) }
    }

    fun onPopularSubscriptionSelected(popular: PopularSubscription) {
        _uiState.update {
            it.copy(
                name = popular.name,
                price = popular.defaultPrice.toString(),
                manageUrl = popular.manageUrl ?: "",
                icon = popular.icon,
                logoUrl = popular.logoUrl
            )
        }
    }

    fun saveSubscription() {
        val currentState = _uiState.value
        if (currentState.name.isBlank() || currentState.price.isBlank() || currentState.renewalDate.isBlank()) return

        viewModelScope.launch {
            val subscription = Subscription(
                id = subscriptionId ?: UUID.randomUUID().toString(),
                name = currentState.name,
                price = currentState.price.toDoubleOrNull() ?: 0.0,
                billingCycle = "Monthly", // Default for now
                category = "General", // Default for now
                status = "Active",
                nextBillingDate = currentState.renewalDate,
                icon = currentState.icon,
                logoUrl = currentState.logoUrl,
                manageUrl = currentState.manageUrl.ifBlank { null }
            )
            
            if (currentState.isEditing) {
                repository.updateSubscription(subscription)
            } else {
                repository.addSubscription(subscription)
            }
            
            // Trigger an immediate check for notifications after adding or updating
            notificationScheduler.runImmediateCheck()

            _event.emit(AddSubscriptionEvent.SaveSuccess)
        }
    }
}

data class AddSubscriptionState(
    val name: String = "",
    val price: String = "",
    val renewalDate: String = "",
    val manageUrl: String = "",
    val icon: String = "star",
    val logoUrl: String? = null,
    val isEditing: Boolean = false,
    val popularSubscriptions: List<PopularSubscription> = emptyList(),
    val popularSearchQuery: String = ""
)

sealed class AddSubscriptionEvent {
    object SaveSuccess : AddSubscriptionEvent()
}
