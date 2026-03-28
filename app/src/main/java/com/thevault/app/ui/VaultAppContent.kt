package com.thevault.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.thevault.app.ui.add.AddSubscriptionScreen
import com.thevault.app.ui.dashboard.DashboardScreen
import com.thevault.app.ui.dashboard.VaultBottomBar
import com.thevault.app.ui.details.SubscriptionDetailsScreen
import com.thevault.app.ui.list.SubscriptionsListScreen
import com.thevault.app.ui.notifications.NotificationsScreen
import com.thevault.app.ui.settings.SettingsScreen

@Composable
fun VaultAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Define which routes should show the bottom bar
    val showBottomBar = when {
        currentDestination?.route == "dashboard" -> true
        currentDestination?.route == "subscriptions" -> true
        currentDestination?.route?.startsWith("add") == true -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                VaultBottomBar(
                    currentRoute = when {
                        currentDestination?.route?.startsWith("add") == true -> "add"
                        else -> currentDestination?.route ?: "dashboard"
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    onNavigateToDetails = { id -> navController.navigate("details/$id") },
                    onNavigateToSettings = { navController.navigate("settings") },
                    onNavigateToNotifications = { navController.navigate("notifications") }
                )
            }
            composable("subscriptions") {
                SubscriptionsListScreen(
                    onNavigateToDetails = { id -> navController.navigate("details/$id") },
                    onNavigateToAdd = { navController.navigate("add") }
                )
            }
            composable(
                route = "add?id={id}",
                arguments = listOf(navArgument("id") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null 
                })
            ) {
                AddSubscriptionScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable("notifications") {
                NotificationsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetails = { id -> navController.navigate("details/$id") }
                )
            }
            composable(
                "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                deepLinks = listOf(navDeepLink { uriPattern = "thevault://details/{id}" })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                SubscriptionDetailsScreen(
                    id = id, 
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { subId -> navController.navigate("add?id=$subId") }
                )
            }
        }
    }
}
