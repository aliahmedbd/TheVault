package com.thevault.app.ui.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
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
import com.thevault.app.ui.theme.TheVaultTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(onNavigateBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var renewalDate by remember { mutableStateOf("") }
    var manageUrl by remember { mutableStateOf("") }

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
                    "Add Subscription",
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

            // Core Details Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    VaultTextField(label = "Subscription Name", value = name, onValueChange = { name = it }, placeholder = "e.g. Netflix")
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            VaultTextField(label = "Monthly Price", value = price, onValueChange = { price = it }, placeholder = "0.00", prefix = "$")
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            VaultTextField(label = "Renewal Date", value = renewalDate, onValueChange = { renewalDate = it }, placeholder = "YYYY-MM-DD")
                        }
                    }

                    VaultTextField(label = "Manage URL (Optional)", value = manageUrl, onValueChange = { manageUrl = it }, placeholder = "https://...")
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
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D64))
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Subscription", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
fun VaultTextField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String, prefix: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF3F484D))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFF70787E).copy(alpha = 0.5f)) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFDEE3EB),
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF004D64)
            ),
            prefix = prefix?.let { { Text(it, fontWeight = FontWeight.Bold, color = Color(0xFF004D64)) } }
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
