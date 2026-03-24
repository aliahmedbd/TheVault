package com.thevault.app.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.thevault.app.data.INITIAL_SUBSCRIPTIONS
import com.thevault.app.ui.dashboard.VaultBottomBar
import com.thevault.app.ui.dashboard.getIconForName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailsScreen(id: String, onNavigateBack: () -> Unit) {
    val sub = INITIAL_SUBSCRIPTIONS.find { it.id == id } ?: INITIAL_SUBSCRIPTIONS[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Vault", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { VaultBottomBar(currentRoute = "subscriptions", onNavigate = { route ->
            if (route == "dashboard") onNavigateBack()
        }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 24.dp, bottomStart = 48.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFF0F4FD))
                    .padding(32.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (sub.logoUrl != null) {
                                AsyncImage(model = sub.logoUrl, contentDescription = null)
                            } else {
                                Icon(getIconForName(sub.icon), contentDescription = null, tint = Color(0xFF004D64), modifier = Modifier.size(40.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF8FEEFC))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006D77))
                            }
                            Text(sub.name, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black, color = Color(0xFF004D64))
                            Text(sub.category + " Plan", color = Color(0xFF3F484D))
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("MONTHLY SPEND", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3F484D))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("$", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D64))
                            Text(sub.price.toInt().toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = Color(0xFF004D64))
                        }
                    }
                }
            }

            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF004D64), Color(0xFF006684))))
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.2f)).padding(12.dp)) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Provider Portal", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("Manage on ${sub.name}.com", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Column {
                        Text("NEXT BILLING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3F484D))
                        Text("Oct 24", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF004D64))
                    }
                }
            }

            // History
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Payment History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { }) {
                        Text("Download All", color = Color(0xFF006972))
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(3) {
                        HistoryItem(sub.price)
                    }
                }
            }

            // Delete
            TextButton(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFBA1A1A))
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Unlink Subscription from Vault", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HistoryItem(price: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE4E8F1)).padding(8.dp)) {
                Icon(Icons.Default.DateRange, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("September 24, 2023", fontWeight = FontWeight.Bold)
                Text("Invoice #NFLX-92834", fontSize = 12.sp, color = Color(0xFF3F484D))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$${price}", fontWeight = FontWeight.Bold, color = Color(0xFF004D64))
                Text("PAID", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF006972))
            }
        }
    }
}
