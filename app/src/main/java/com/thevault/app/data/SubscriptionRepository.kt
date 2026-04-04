package com.thevault.app.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepository @Inject constructor(
    private val subscriptionDao: SubscriptionDao,
    private val popularSubscriptionDataSource: PopularSubscriptionDataSource
) {
    fun getAllSubscriptions(): Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()

    suspend fun getSubscriptionById(id: String): Subscription? = subscriptionDao.getSubscriptionById(id)

    suspend fun addSubscription(subscription: Subscription) {
        subscriptionDao.insertSubscription(subscription)
    }

    suspend fun updateSubscription(subscription: Subscription) {
        subscriptionDao.updateSubscription(subscription)
    }

    suspend fun deleteSubscription(id: String) {
        subscriptionDao.deleteSubscriptionById(id)
    }

    fun getPopularSubscriptions(query: String = ""): List<PopularSubscription> {
        val all = popularSubscriptionDataSource.getPopularSubscriptions()
        if (query.isBlank()) return all
        return all.filter { it.name.contains(query, ignoreCase = true) }
    }
}
