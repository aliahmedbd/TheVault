package com.thevault.app.domain

import com.thevault.app.data.SubscriptionRepository
import javax.inject.Inject

class DeleteSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteSubscription(id)
    }
}
