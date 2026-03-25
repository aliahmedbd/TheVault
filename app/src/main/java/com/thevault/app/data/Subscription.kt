package com.thevault.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey val id: String,
    val name: String,
    val price: Double,
    val billingCycle: String,
    val category: String,
    val status: String,
    val nextBillingDate: String,
    val icon: String,
    val logoUrl: String? = null,
    val manageUrl: String? = null
)
