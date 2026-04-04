package com.thevault.app.domain

import com.thevault.app.data.PopularSubscription
import com.thevault.app.data.SubscriptionRepository
import javax.inject.Inject

class GetPopularSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    operator fun invoke(query: String = ""): List<PopularSubscription> = repository.getPopularSubscriptions(query)
}
