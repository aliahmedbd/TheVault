package com.thevault.app.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.thevault.app.data.Subscription
import com.thevault.app.ui.dashboard.getIconForName
import com.thevault.app.ui.theme.TheVaultTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SubscriptionDetailsScreen(
    id: String,
    onNavigateBack: () -> Unit,
    viewModel: SubscriptionDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    SubscriptionDetailsContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onDeleteClick = {
            viewModel.deleteSubscription()
            onNavigateBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailsContent(
    state: SubscriptionDetailsState,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sub = state.subscription
    val uriHandler = LocalUriHandler.current

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
        }
    ) { padding ->
        if (sub == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Subscription not found")
                }
            }
        } else {
            val formattedNextBillingDate = remember(sub.nextBillingDate) {
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                    val date = inputFormat.parse(sub.nextBillingDate)
                    if (date != null) outputFormat.format(date) else sub.nextBillingDate
                } catch (e: Exception) {
                    sub.nextBillingDate
                }
            }

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
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
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

                            Column(horizontalAlignment = Alignment.End) {
                                Text("MONTHLY SPEND", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3F484D))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text("$", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D64))
                                    Text(sub.price.toInt().toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = Color(0xFF004D64))
                                }
                            }
                        }

                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF8FEEFC))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006D77))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                sub.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF004D64)
                            )
                            Text(sub.category + " Plan", color = Color(0xFF3F484D))
                        }
                    }
                }

                // Actions
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF004D64), Color(0xFF006684))))
                            .clickable(enabled = sub.manageUrl != null) {
                                sub.manageUrl?.let { url ->
                                    val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                        "https://$url"
                                    } else {
                                        url
                                    }
                                    try {
                                        uriHandler.openUri(formattedUrl)
                                    } catch (e: Exception) {
                                        // Handle error
                                    }
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)).padding(8.dp)) {
                                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Provider Portal", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                Text(if (sub.manageUrl != null) "Manage" else "No URL", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("NEXT BILLING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3F484D))
                            Text(formattedNextBillingDate, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF004D64))
                        }
                    }
                }

                // Delete
                TextButton(
                    onClick = onDeleteClick,
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
}

@Preview(showBackground = true)
@Composable
fun SubscriptionDetailsPreview() {
    val sampleSub = Subscription(
        id = "1",
        name = "Google One",
        price = 1.99,
        billingCycle = "Monthly",
        category = "Cloud",
        status = "Active",
        nextBillingDate = "2024-01-01",
        icon = "cloud",
        manageUrl = "https://one.google.com"
    )
    TheVaultTheme {
        SubscriptionDetailsContent(
            state = SubscriptionDetailsState(subscription = sampleSub),
            onNavigateBack = {},
            onDeleteClick = {}
        )
    }
}
