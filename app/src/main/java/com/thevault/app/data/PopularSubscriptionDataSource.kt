package com.thevault.app.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularSubscriptionDataSource @Inject constructor() {
    fun getPopularSubscriptions(): List<PopularSubscription> {
        return listOf(
            PopularSubscription(
                id = "p1",
                name = "Netflix",
                defaultPrice = 15.99,
                category = "Entertainment",
                icon = "play_circle",
                logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDwpJbturjOYENjNjnYKM7PHNTqhgPJhGgdsQcKGPntxo3uc8vr2JuW6nFgSUM4vGdCA74YO2fKXDll8paSTUFf3aO5yyn7DfT_316gpFkok2oGtZzUgmJeBNYaTt3VpDHojzu17geGg9iOzn7iy9rwM_07WpsDHoc3mCJkdzibKErMGcIYLPiwMqssAvGFps0dY7XjsImwaFhoC6JKVcexRtLluinQKSmC2FckiqOZW62vN4WJIit7n3FMA-klO9EDAbJyAOyKedI",
                manageUrl = "https://www.netflix.com"
            ),
            PopularSubscription(
                id = "p2",
                name = "Spotify",
                defaultPrice = 9.99,
                category = "Music",
                icon = "music_note",
                logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA_1tMgah6WvQELXSuaQ81AzHizaSYhN1CY5seme36UD32gCxYgUPtVq7TrXn5GL5Btb7TYMbHkFvX8WErUE3vUtvUb7o7DbO0AiAJhWCrrR4U5Rj3fDSAtz-EzGyP4QqNpazWmH4CVGU0y3ttfiIt9WwBSQrSDraQlK92oQYtuo1EPZ0FrfIJh0UCC29mpp-Z7oInsc2Ndn04BGjh4V-aHhuiP_q9MUkUCENLFs5PMHJbyPDIKIWif3S4tj-INVE1PjiC7NSDCd7I",
                manageUrl = "https://www.spotify.com"
            ),
            PopularSubscription(
                id = "p3",
                name = "Adobe CC",
                defaultPrice = 52.99,
                category = "Creative",
                icon = "cloud",
                logoUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuApUPFLvW42GVCxmdy3Z_Jnm_OQWRDogWwWn9QY9VdkeMESWLUzYLDWJY4547OHYEq3ITxR9EjSBfGSNNedvX5U5WN5BLm9YNW-M5PyelRuo_S1eOTirPZTUvLfbNCYNdVsxNsRqj8nu_Bz3pz_pSb8qnAcV-F2KXthnFTV48tiZia2arDRAdk-EF0MwczF29FUafZSwlWZEGiLjTd3lhHxRXKelsbjFLnTnW_KdrRrAG11VnSNpcAZmYlVTksNJj0Abz18ebLKhRY",
                manageUrl = "https://www.adobe.com"
            ),
            PopularSubscription(
                id = "p4",
                name = "Google One",
                defaultPrice = 1.99,
                category = "Cloud",
                icon = "cloud",
                manageUrl = "https://one.google.com"
            ),
            PopularSubscription(
                id = "p5",
                name = "Disney+",
                defaultPrice = 7.99,
                category = "Entertainment",
                icon = "play_circle",
                manageUrl = "https://www.disneyplus.com"
            )
        )
    }
}
