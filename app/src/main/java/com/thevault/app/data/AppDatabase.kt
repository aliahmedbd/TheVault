package com.thevault.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Subscription::class, Notification::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun notificationDao(): NotificationDao
}
