package com.thevault.app.data

data class PopularSubscription(
    val id: String,
    val name: String,
    val defaultPrice: Double,
    val category: String,
    val icon: String,
    val logoUrl: String? = null,
    val manageUrl: String? = null
)
