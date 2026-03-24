package com.thevault.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.thevault.app.data.Subscription

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit,
    onNavigateToSubscriptions: () -> Unit,
    onNavigateToDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { VaultTopBar() },
        bottomBar = { VaultBottomBar(currentRoute = "dashboard", onNavigate = { route ->
            when(route) {
                "add" -> onNavigateToAdd()
                "subscriptions" -> onNavigateToSubscriptions()
                "dashboard" -> {}
            }
        }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Monthly Spend Hero
            item {
                SpendHeroSection(state.totalMonthlySpend, state.subscriptions.size, state.savedThisMonth)
            }

            // Upcoming Renewals
            item {
                UpcomingRenewalsSection(state.subscriptions)
            }

            // Active Subscriptions
            item {
                ActiveSubscriptionsSection(state.subscriptions, onNavigateToDetails)
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultTopBar() {
    TopAppBar(
        title = {
            Text(
                "The Vault",
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp,
                color = Color(0xFF001F2A)
            )
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDQsEBxzjypyD-uQjEDempgexehAeNF2rDz7Tb2W4qIX-2QwuPhBJuFmJFqhqMW2akGH9EIC9BFuYgxpoULLcRrRuh-F8hS-TrZUbv5n-WCbR7nYUmyUjnFS_PB87RSNh5F18pVaiYqhaTZt3rXBg-plGnqUpAPefd7malqNXsupX6L1hfOA5wXNLy-Kkg6bIBWqL_k2Fis4QxYFyGb6w48DG1jyu7dTahw5m4gTbCDPWyVSvki0Uo5MzMCBuEiuGG2gti1rNnaAVI",
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    )
}

@Composable
fun SpendHeroSection(total: Double, count: Int, saved: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF004D64), Color(0xFF006684))
                )
            )
            .padding(32.dp)
    ) {
        Column {
            Text(
                "TOTAL MONTHLY SPEND",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f),
                letterSpacing = 2.sp
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "$${total.toInt()}",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    ".${(total % 1 * 100).toInt().toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoCard(label = "SUBSCRIPTIONS", value = count.toString())
                InfoCard(label = "SAVED THIS MONTH", value = "$${saved.toInt()}.00")
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun UpcomingRenewalsSection(subscriptions: List<Subscription>) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Upcoming Renewals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            TextButton(onClick = { }) {
                Text("View Calendar", color = Color(0xFF006972), fontWeight = FontWeight.SemiBold)
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 24.dp)
        ) {
            items(subscriptions) { sub ->
                RenewalCard(sub)
            }
        }
    }
}

@Composable
fun RenewalCard(sub: Subscription) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(176.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF0F4FD))
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        getIconForName(sub.icon),
                        contentDescription = null,
                        tint = Color(0xFF004D64),
                        modifier = Modifier.size(30.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(full = true))
                        .background(Color(0xFF9C4400))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        "IN 2 DAYS",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column {
                Text(
                    sub.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$${sub.price}/mo",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF3F484D)
                )
            }
        }
    }
}

@Composable
fun ActiveSubscriptionsSection(subscriptions: List<Subscription>, onNavigateToDetails: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Active Subscriptions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            IconButton(
                onClick = { },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE4E8F1))
            ) {
                Icon(Icons.Default.List, contentDescription = "Filter")
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            subscriptions.forEach { sub ->
                SubscriptionListItem(sub, onNavigateToDetails)
            }
        }
    }
}

@Composable
fun SubscriptionListItem(sub: Subscription, onClick: (String) -> Unit) {
    Surface(
        onClick = { onClick(sub.id) },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F4FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    getIconForName(sub.icon),
                    contentDescription = null,
                    tint = Color(0xFF004D64)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    sub.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "${sub.billingCycle} • ${sub.category}",
                    fontSize = 12.sp,
                    color = Color(0xFF3F484D)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${sub.price}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D64),
                    fontSize = 16.sp
                )
                Text(
                    "ACTIVE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F484D)
                )
            }
        }
    }
}

@Composable
fun VaultBottomBar(currentRoute: String, onNavigate: (String) -> Unit = {}) {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.8f),
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { onNavigate("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("DASHBOARD", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "subscriptions",
            onClick = { onNavigate("subscriptions") },
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("SUBSCRIPTIONS", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = currentRoute == "add",
            onClick = { onNavigate("add") },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
            label = { Text("ADD", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        )
    }
}

fun getIconForName(name: String): ImageVector {
    return when (name) {
        "cloud" -> Icons.Default.Cloud
        "play_circle" -> Icons.Default.PlayArrow
        "music_note" -> Icons.Default.MusicNote
        "mail" -> Icons.Default.Email
        "dumbbell" -> Icons.Default.Info
        "package" -> Icons.Default.ShoppingCart
        "database" -> Icons.Default.Build
        else -> Icons.Default.Star
    }
}
