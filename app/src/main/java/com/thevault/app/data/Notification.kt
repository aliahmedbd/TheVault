package com.thevault.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: String,
    val subscriptionId: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
