package com.thevault.app.ui.dashboard

import com.thevault.app.data.Subscription

data class DashboardState(
    val subscriptions: List<Subscription> = emptyList(),
    val totalMonthlySpend: Double = 0.0,
    val savedThisMonth: Double = 0.0,
    val unreadNotificationCount: Int = 0,
    val isLoading: Boolean = false
)

sealed class DashboardIntent {
    object LoadSubscriptions : DashboardIntent()
    data class DeleteSubscription(val id: String) : DashboardIntent()
}

sealed class DashboardEffect {
    data class ShowError(val message: String) : DashboardEffect()
}
