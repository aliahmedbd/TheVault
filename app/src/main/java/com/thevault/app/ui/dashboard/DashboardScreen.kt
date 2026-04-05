package com.thevault.app.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.thevault.app.data.Subscription
import com.thevault.app.ui.theme.TheVaultTheme
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    DashboardContent(
        state = state,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToCalendar = onNavigateToCalendar,
        onEditClick = onNavigateToEdit,
        onDeleteClick = { viewModel.deleteSubscription(it) }
    )
}

@Composable
fun DashboardContent(
    state: DashboardState,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    var subscriptionToDelete by remember { mutableStateOf<Subscription?>(null) }

    if (subscriptionToDelete != null) {
        AlertDialog(
            onDismissRequest = { subscriptionToDelete = null },
            title = { Text("Unlink Subscription") },
            text = { Text("Are you sure you want to remove ${subscriptionToDelete?.name} from your vault?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        subscriptionToDelete?.id?.let { onDeleteClick(it) }
                        subscriptionToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFBA1A1A))
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { subscriptionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = { 
            VaultTopBar(
                unreadCount = state.unreadNotificationCount,
                onSettingsClick = onNavigateToSettings,
                onNotificationsClick = onNavigateToNotifications
            ) 
        }
    ) { padding ->
        if (state.subscriptions.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No subscriptions yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        "Tap '+' to add your first one",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            }
        } else {
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
                if (state.upcomingSubscriptions.isNotEmpty()) {
                    item {
                        UpcomingRenewalsSection(
                            subscriptions = state.upcomingSubscriptions,
                            onViewCalendarClick = onNavigateToCalendar,
                            onEditClick = onEditClick,
                            onDeleteClick = { subscriptionToDelete = it }
                        )
                    }
                }

                // Active Subscriptions
                item {
                    ActiveSubscriptionsSection(state.subscriptions, onNavigateToDetails)
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultTopBar(
    unreadCount: Int,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
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
            BadgedBox(
                badge = {
                    if (unreadCount > 0) {
                        Badge {
                            Text(unreadCount.toString())
                        }
                    }
                }
            ) {
                IconButton(onClick = onNotificationsClick) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
            
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
            Spacer(modifier = Modifier.width(8.dp))
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
                InfoCard(label = "SUBSCRIPTIONS", value = count.toString(), modifier = Modifier.weight(1f))
                InfoCard(label = "SAVED THIS MONTH", value = "$${saved.toInt()}.00", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
fun UpcomingRenewalsSection(
    subscriptions: List<Subscription>,
    onViewCalendarClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Subscription) -> Unit
) {
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
            TextButton(onClick = onViewCalendarClick) {
                Text("View Calendar", color = Color(0xFF006972), fontWeight = FontWeight.SemiBold)
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 24.dp)
        ) {
            items(subscriptions) { sub ->
                RenewalCard(
                    sub = sub,
                    onEditClick = { onEditClick(sub.id) },
                    onDeleteClick = { onDeleteClick(sub) }
                )
            }
        }
    }
}

@Composable
fun RenewalCard(
    sub: Subscription,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val daysLeft = remember(sub.nextBillingDate) {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            val date = dateFormat.parse(sub.nextBillingDate)
            if (date != null) {
                val diff = date.time - today.time
                TimeUnit.MILLISECONDS.toDays(diff).toInt()
            } else 0
        } catch (e: Exception) {
            0
        }
    }

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
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF004D64), modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFBA1A1A), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (daysLeft <= 2) Color(0xFFBA1A1A) else Color(0xFF9C4400))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (daysLeft == 0) "TODAY" else "IN $daysLeft DAYS",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val sampleSub = Subscription(
        id = "1",
        name = "Netflix",
        price = 15.99,
        billingCycle = "Monthly",
        category = "Entertainment",
        status = "Active",
        nextBillingDate = "2024-01-01",
        icon = "play_circle"
    )
    TheVaultTheme {
        DashboardContent(
            state = DashboardState(
                subscriptions = listOf(sampleSub),
                upcomingSubscriptions = listOf(sampleSub),
                totalMonthlySpend = 15.99,
                savedThisMonth = 0.0,
                unreadNotificationCount = 2
            ),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onNavigateToNotifications = {},
            onNavigateToCalendar = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardEmptyPreview() {
    TheVaultTheme {
        DashboardContent(
            state = DashboardState(
                subscriptions = emptyList(),
                upcomingSubscriptions = emptyList(),
                totalMonthlySpend = 0.0,
                savedThisMonth = 0.0,
                unreadNotificationCount = 0
            ),
            onNavigateToDetails = {},
            onNavigateToSettings = {},
            onNavigateToNotifications = {},
            onNavigateToCalendar = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
