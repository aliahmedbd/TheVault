package com.thevault.app.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.thevault.app.data.Notification
import com.thevault.app.data.NotificationRepository
import com.thevault.app.data.SubscriptionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@HiltWorker
class SubscriptionNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val subscriptionRepository: SubscriptionRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): androidx.work.ListenableWorker.Result {
        val subscriptions = subscriptionRepository.getAllSubscriptions().first()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        for (subscription in subscriptions) {
            try {
                val nextBillingDate = dateFormat.parse(subscription.nextBillingDate)
                if (nextBillingDate != null) {
                    val diffInMillis = nextBillingDate.time - today.time
                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

                    if (diffInDays in 0..7) {
                        // Create a unique ID based on subscription and billing date to avoid duplicates
                        val notificationId = "${subscription.id}_${subscription.nextBillingDate}"
                        
                        val title = "Subscription Reminder"
                        val message = "${subscription.name} subscription is ending within $diffInDays days"
                        
                        // Show system notification
                        notificationHelper.showSubscriptionReminder(
                            subscription.id,
                            subscription.name,
                            diffInDays
                        )

                        // Save to database for in-app list
                        notificationRepository.insertNotification(
                            Notification(
                                id = notificationId,
                                subscriptionId = subscription.id,
                                title = title,
                                message = message,
                                timestamp = System.currentTimeMillis(),
                                isRead = false
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                // Ignore parsing errors
            }
        }

        return androidx.work.ListenableWorker.Result.success()
    }
}
