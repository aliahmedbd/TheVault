package com.thevault.app.ui.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import com.thevault.app.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subscriptionId: String? = savedStateHandle["id"]

    private val _uiState = MutableStateFlow(AddSubscriptionState())
    val uiState: StateFlow<AddSubscriptionState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AddSubscriptionEvent>()
    val event = _event.asSharedFlow()

    init {
        subscriptionId?.let { id ->
            viewModelScope.launch {
                repository.getSubscriptionById(id)?.let { sub ->
                    _uiState.update {
                        it.copy(
                            name = sub.name,
                            price = sub.price.toString(),
                            renewalDate = sub.nextBillingDate,
                            manageUrl = sub.manageUrl ?: "",
                            isEditing = true
                        )
                    }
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
                icon = "star", // Default icon
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
    val isEditing: Boolean = false
)

sealed class AddSubscriptionEvent {
    object SaveSuccess : AddSubscriptionEvent()
}
