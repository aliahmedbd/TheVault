package com.thevault.app.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<AddSubscriptionEvent>()
    val event = _event.asSharedFlow()

    fun saveSubscription(name: String, price: String, renewalDate: String, manageUrl: String) {
        if (name.isBlank() || price.isBlank() || renewalDate.isBlank()) return

        viewModelScope.launch {
            val subscription = Subscription(
                id = UUID.randomUUID().toString(),
                name = name,
                price = price.toDoubleOrNull() ?: 0.0,
                billingCycle = "Monthly", // Default for now
                category = "General", // Default for now
                status = "Active",
                nextBillingDate = renewalDate,
                icon = "star", // Default icon
                manageUrl = manageUrl.ifBlank { null }
            )
            repository.addSubscription(subscription)
            _event.emit(AddSubscriptionEvent.SaveSuccess)
        }
    }
}

sealed class AddSubscriptionEvent {
    object SaveSuccess : AddSubscriptionEvent()
}
