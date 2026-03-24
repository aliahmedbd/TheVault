package com.thevault.app.data

data class Subscription(
    val id: String,
    val name: String,
    val price: Double,
    val billingCycle: String,
    val category: String,
    val status: String,
    val nextBillingDate: String,
    val icon: String,
    val logoUrl: String? = null
)

val INITIAL_SUBSCRIPTIONS = listOf(
    Subscription(
        id = "1",
        name = "Adobe Creative Cloud",
        price = 52.99,
        billingCycle = "Monthly",
        category = "Creative",
        status = "Reviewing",
        nextBillingDate = "2023-10-30",
        icon = "cloud",
        logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuApUPFLvW42GVCxmdy3Z_Jnm_OQWRDogWwWn9QY9VdkeMESWLUzYLDWJY4547OHYEq3ITxR9EjSBfGSNNedvX5U5WN5BLm9YNW-M5PyelRuo_S1eOTirPZTUvLfbNCYNdVsxNsRqj8nu_Bz3pz_pSb8qnAcV-F2KXthnFTV48tiZia2arDRAdk-EF0MwczF29FUafZSwlWZEGiLjTd3lhHxRXKelsbjFLnTnW_KdrRrAG11VnSNpcAZmYlVTksNJj0Abz18ebLKhRY"
    ),
    Subscription(
        id = "2",
        name = "Netflix Premium",
        price = 19.99,
        billingCycle = "Monthly",
        category = "Entertainment",
        status = "Active",
        nextBillingDate = "2023-10-24",
        icon = "play_circle",
        logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDwpJbturjOYENjNjnYKM7PHNTqhgPJhGgdsQcKGPntxo3uc8vr2JuW6nFgSUM4vGdCA74YO2fKXDll8paSTUFf3aO5yyn7DfT_316gpFkok2oGtZzUgmJeBNYaTt3VpDHojzu17geGg9iOzn7iy9rwM_07WpsDHoc3mCJkdzibKErMGcIYLPiwMqssAvGFps0dY7XjsImwaFhoC6JKVcexRtLluinQKSmC2FckiqOZW62vN4WJIit7n3FMA-klO9EDAbJyAOyKedI"
    ),
    Subscription(
        id = "3",
        name = "Spotify Family",
        price = 16.99,
        billingCycle = "Monthly",
        category = "Entertainment",
        status = "Active",
        nextBillingDate = "2023-12-14",
        icon = "music_note",
        logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA_1tMgah6WvQELXSuaQ81AzHizaSYhN1CY5seme36UD32gCxYgUPtVq7TrXn5GL5Btb7TYMbHkFvX8WErUE3vUtvUb7o7DbO0AiAJhWCrrR4U5Rj3fDSAtz-EzGyP4QqNpazWmH4CVGU0y3ttfiIt9WwBSQrSDraQlK92oQYtuo1EPZ0FrfIJh0UCC29mpp-Z7oInsc2Ndn04BGjh4V-aHhuiP_q9MUkUCENLFs5PMHJbyPDIKIWif3S4tj-INVE1PjiC7NSDCd7I"
    )
)
