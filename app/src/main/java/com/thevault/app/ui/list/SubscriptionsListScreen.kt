package com.thevault.app.ui.list

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thevault.app.data.INITIAL_SUBSCRIPTIONS
import com.thevault.app.ui.dashboard.SubscriptionListItem
import com.thevault.app.ui.dashboard.VaultBottomBar
import com.thevault.app.ui.theme.TheVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsListScreen(onNavigateToDetails: (String) -> Unit) {
    var search by remember { mutableStateOf("") }
    val filters = listOf("All", "Monthly", "Yearly", "Entertainment", "SaaS", "Tech")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Vault", fontWeight = FontWeight.Black) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { VaultBottomBar(currentRoute = "subscriptions", onNavigate = { route ->
            // Handle nav
        }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFF004D64),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Column {
                    Text("Subscriptions", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
                    Text("Manage your recurring digital lifestyle.", color = Color(0xFF3F484D))
                }
            }

            // Search
            item {
                TextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Search services...") },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFDEE3EB),
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF004D64)
                    )
                )
            }

            // Filters
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = filter == "All",
                            onClick = { },
                            label = { Text(filter, fontWeight = FontWeight.Bold) },
                            shape = CircleShape
                        )
                    }
                }
            }

            // List
            items(INITIAL_SUBSCRIPTIONS) { sub ->
                SubscriptionListItem(sub, onNavigateToDetails)
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubscriptionsListPreview() {
    TheVaultTheme {
        SubscriptionsListScreen(onNavigateToDetails = {})
    }
}
