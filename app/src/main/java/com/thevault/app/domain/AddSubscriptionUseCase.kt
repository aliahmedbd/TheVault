package com.thevault.app.domain

import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(subscription: Subscription) {
        repository.addSubscription(subscription)
    }
}
