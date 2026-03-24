package com.thevault.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Subscription::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
}
