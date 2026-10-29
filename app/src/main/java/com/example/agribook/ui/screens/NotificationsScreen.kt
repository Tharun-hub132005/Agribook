package com.example.agribook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AgriBookViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationsScreen(viewModel: AgriBookViewModel) {
    val notifications by viewModel.notifications.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val timeFormat = remember { SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AgriStrings.get("notifications", currentLang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGreenPrimary
                )
                Text(
                    text = "Agricultural community activity updates",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )
            }

            if (notifications.any { !it.isRead }) {
                TextButton(
                    onClick = { viewModel.markNotificationsAsRead() },
                    colors = ButtonDefaults.textButtonColors(contentColor = AgriLime)
                ) {
                    Text("Mark all read", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = null,
                        tint = AgriTextSecondary,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No notifications yet",
                        color = AgriTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Activity on your farming queries and network will appear here.",
                        color = AgriTextSecondary,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notifications, key = { it.id }) { notif ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!notif.isRead) AgriSurfaceCardElevated else AgriSurfaceCard
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (!notif.isRead) AgriGreenPrimary.copy(alpha = 0.5f) else AgriBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notification_card_${notif.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (notif.type) {
                                            "LIKE" -> AgriGreenContainer
                                            "COMMENT" -> AgriGreenContainer
                                            "CONNECTION_REQUEST" -> AgriGoldContainer
                                            else -> AgriSurfaceDark
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "LIKE" -> Icons.Default.ThumbUp
                                        "COMMENT" -> Icons.Default.Comment
                                        "CONNECTION_REQUEST" -> Icons.Default.PersonAdd
                                        else -> Icons.Default.Info
                                    },
                                    contentDescription = null,
                                    tint = when (notif.type) {
                                        "LIKE" -> AgriGreenPrimary
                                        "COMMENT" -> AgriLime
                                        "CONNECTION_REQUEST" -> AgriGold
                                        else -> AgriTextPrimary
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AgriTextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.body,
                                    fontSize = 12.sp,
                                    color = AgriTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = timeFormat.format(Date(notif.timestamp)),
                                    fontSize = 10.sp,
                                    color = AgriTextTertiary
                                )
                            }

                            if (!notif.isRead) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AgriGreenPrimary)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
