package com.thevault.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thevault.app.data.Subscription
import com.thevault.app.ui.dashboard.getIconForName
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Renewal Calendar", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CalendarHeader(
                currentMonth = currentMonth,
                onPreviousMonth = {
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, -1)
                    currentMonth = newMonth
                },
                onNextMonth = {
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, 1)
                    currentMonth = newMonth
                }
            )

            CalendarGrid(
                currentMonth = currentMonth,
                subscriptions = state.subscriptions
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Renewals this month",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val monthSubscriptions = remember(currentMonth, state.subscriptions) {
                state.subscriptions.filter { sub ->
                    val cal = Calendar.getInstance()
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    try {
                        sdf.parse(sub.nextBillingDate)?.let { date ->
                            cal.time = date
                            cal.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                                    cal.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
                        } ?: false
                    } catch (e: Exception) {
                        false
                    }
                }.sortedBy { it.nextBillingDate }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (monthSubscriptions.isEmpty()) {
                    item {
                        Text(
                            "No renewals scheduled for this month.",
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(monthSubscriptions) { sub ->
                        CalendarSubscriptionListItem(sub = sub, onClick = onNavigateToDetails)
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarSubscriptionListItem(sub: Subscription, onClick: (String) -> Unit) {
    val day = remember(sub.nextBillingDate) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(sub.nextBillingDate)
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
                cal.get(Calendar.DAY_OF_MONTH).toString()
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
    
    val month = remember(sub.nextBillingDate) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(sub.nextBillingDate)
            val monthSdf = SimpleDateFormat("MMM", Locale.getDefault())
            if (date != null) monthSdf.format(date).uppercase() else ""
        } catch (e: Exception) {
            ""
        }
    }

    Surface(
        onClick = { onClick(sub.id) },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date Badge
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F4FD))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = month,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004D64)
                )
                Text(
                    text = day,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF004D64)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F4FD).copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    getIconForName(sub.icon),
                    contentDescription = null,
                    tint = Color(0xFF004D64),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Name and category
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    sub.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    sub.category,
                    fontSize = 12.sp,
                    color = Color(0xFF3F484D)
                )
            }
            
            // Price
            Text(
                text = "$${sub.price}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF004D64),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun CalendarHeader(
    currentMonth: Calendar,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
        }
        Text(
            text = monthYearFormat.format(currentMonth.time),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next")
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: Calendar,
    subscriptions: List<Subscription>
) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }.get(Calendar.DAY_OF_WEEK)
    val offset = firstDayOfWeek - 1

    val days = (1..daysInMonth).toList()
    val totalCells = ((daysInMonth + offset + 6) / 7) * 7

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        for (row in 0 until totalCells / 7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val dayNum = index - offset + 1
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNum in 1..daysInMonth) {
                            val isToday = isSameDay(Calendar.getInstance(), currentMonth, dayNum)
                            val hasRenewal = subscriptions.any { sub ->
                                try {
                                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val date = sdf.parse(sub.nextBillingDate)
                                    val cal = Calendar.getInstance()
                                    if (date != null) {
                                        cal.time = date
                                        cal.get(Calendar.DAY_OF_MONTH) == dayNum &&
                                                cal.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                                                cal.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
                                    } else false
                                } catch (e: Exception) {
                                    false
                                }
                            }

                            if (isToday) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color(0xFF004D64).copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayNum.toString(),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF004D64)
                                    )
                                }
                            } else {
                                Text(text = dayNum.toString())
                            }

                            if (hasRenewal) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 4.dp)
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFBA1A1A))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun isSameDay(today: Calendar, currentMonth: Calendar, dayNum: Int): Boolean {
    return today.get(Calendar.DAY_OF_MONTH) == dayNum &&
            today.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
            today.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
}
