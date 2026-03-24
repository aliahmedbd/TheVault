package com.thevault.app.domain

import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    operator fun invoke(): Flow<List<Subscription>> = repository.getAllSubscriptions()
}
