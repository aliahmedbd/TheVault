package com.thevault.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thevault.app.ui.dashboard.DashboardScreen
import com.thevault.app.ui.add.AddSubscriptionScreen
import com.thevault.app.ui.details.SubscriptionDetailsScreen
import com.thevault.app.ui.list.SubscriptionsListScreen

@Composable
fun VaultAppContent() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToSubscriptions = { navController.navigate("subscriptions") },
                onNavigateToDetails = { id -> navController.navigate("details/$id") }
            )
        }
        composable("subscriptions") {
            SubscriptionsListScreen(
                onNavigateToDetails = { id -> navController.navigate("details/$id") }
            )
        }
        composable("add") {
            AddSubscriptionScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            SubscriptionDetailsScreen(id = id, onNavigateBack = { navController.popBackStack() })
        }
    }
}
