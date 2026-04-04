package com.thevault.app.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.thevault.app.data.PopularSubscription
import com.thevault.app.ui.dashboard.getIconForName
import com.thevault.app.ui.theme.TheVaultTheme
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddSubscriptionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AddSubscriptionEvent.SaveSuccess -> onNavigateBack()
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        viewModel.onRenewalDateChange(sdf.format(Date(millis)))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Edit Subscription" else "The Vault", fontWeight = FontWeight.Black) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Column {
                Text(
                    if (uiState.isEditing) "Edit Subscription" else "Add Subscription",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF004D64)
                )
                Text(
                    "Input your recurring payment details to start tracking in your vault.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF3F484D)
                )
            }

            if (!uiState.isEditing) {
                PopularSubscriptionsSection(
                    populars = uiState.popularSubscriptions,
                    searchQuery = uiState.popularSearchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    onSelected = viewModel::onPopularSubscriptionSelected
                )
            }

            // Core Details Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    VaultTextField(
                        label = "Subscription Name",
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        placeholder = "e.g. Netflix"
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            VaultTextField(
                                label = "Monthly Price",
                                value = uiState.price,
                                onValueChange = viewModel::onPriceChange,
                                placeholder = "0.00",
                                prefix = "$"
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            VaultTextField(
                                label = "Renewal Date",
                                value = uiState.renewalDate,
                                onValueChange = viewModel::onRenewalDateChange,
                                placeholder = "YYYY-MM-DD",
                                readOnly = true,
                                modifier = Modifier.clickable { showDatePicker = true }
                            )
                        }
                    }

                    VaultTextField(
                        label = "Manage URL (Optional)",
                        value = uiState.manageUrl,
                        onValueChange = viewModel::onManageUrlChange,
                        placeholder = "https://..."
                    )
                }
            }

            // Alert Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF0F4FD))
                    .padding(32.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF9C4400).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF773200))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Cancellation Alert", fontWeight = FontWeight.Bold)
                                Text("Notify me before charge", fontSize = 12.sp, color = Color(0xFF3F484D))
                            }
                        }
                        Switch(checked = true, onCheckedChange = { })
                    }
                }
            }

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.saveSubscription() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D64))
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (uiState.isEditing) "Update Subscription" else "Save Subscription", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PopularSubscriptionsSection(
    populars: List<PopularSubscription>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSelected: (PopularSubscription) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Popular Services",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D64)
            )
        }

        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search popular services...") },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDEE3EB),
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF004D64)
            ),
            singleLine = true
        )

        if (populars.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No matching services found", color = Color.Gray)
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(populars) { popular ->
                    PopularItem(popular = popular, onClick = { onSelected(popular) })
                }
            }
        }
    }
}

@Composable
fun PopularItem(popular: PopularSubscription, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 120.dp, height = 140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F4FD)),
                contentAlignment = Alignment.Center
            ) {
                if (popular.logoUrl != null) {
                    AsyncImage(
                        model = popular.logoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                } else {
                    Icon(
                        getIconForName(popular.icon),
                        contentDescription = null,
                        tint = Color(0xFF004D64),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                popular.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF004D64)
            )
            Text(
                "$${popular.defaultPrice}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun VaultTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    prefix: String? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF3F484D))
        TextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = { Text(placeholder, color = Color(0xFF70787E).copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            enabled = !readOnly,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDEE3EB),
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF004D64),
                disabledContainerColor = Color(0xFFDEE3EB),
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = LocalContentColor.current
            ),
            prefix = prefix?.let { { Text(it, fontWeight = FontWeight.Bold, color = Color(0xFF004D64)) } },
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddSubscriptionPreview() {
    TheVaultTheme {
        AddSubscriptionScreen(onNavigateBack = {})
    }
}
