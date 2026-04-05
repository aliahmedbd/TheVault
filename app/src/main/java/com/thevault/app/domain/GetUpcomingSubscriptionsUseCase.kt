package com.thevault.app.domain

import com.thevault.app.data.Subscription
import com.thevault.app.data.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetUpcomingSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository
) {
    operator fun invoke(): Flow<List<Subscription>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        return repository.getAllSubscriptions().map { subscriptions ->
            subscriptions.filter { subscription ->
                try {
                    val nextBillingDate = dateFormat.parse(subscription.nextBillingDate)
                    if (nextBillingDate != null) {
                        val diffInMillis = nextBillingDate.time - today.time
                        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                        diffInDays in 0..7
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    false
                }
            }.sortedBy { it.nextBillingDate }
        }
    }
}
