package com.example.agribook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.agribook.data.local.MessageEntity
import com.example.agribook.localization.AgriStrings
import com.example.agribook.ui.AgriBookViewModel
import com.example.agribook.ui.AppTab
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(viewModel: AgriBookViewModel) {
    val activePartner by viewModel.activeChatPartner.collectAsState()
    val farmers by viewModel.farmers.collectAsState()
    val acceptedConnections by viewModel.acceptedConnections.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    val connectedUserIds = remember(acceptedConnections, currentUser) {
        val uid = currentUser?.id ?: 0L
        acceptedConnections.map {
            if (it.requesterId == uid) it.receiverId else it.requesterId
        }.toSet()
    }

    val connectedFarmers = remember(farmers, connectedUserIds) {
        farmers.filter { connectedUserIds.contains(it.id) || it.role == "EXPERT" }
    }

    if (activePartner == null) {
        // Conversation List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AgriBlack)
                .padding(16.dp)
        ) {
            Text(
                text = "Agricultural Chat Threads",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AgriGreenPrimary
            )
            Text(
                text = "Connect and consult directly with verified farmers & agronomists",
                fontSize = 12.sp,
                color = AgriTextSecondary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (connectedFarmers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = AgriTextSecondary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "No active conversations yet.",
                            color = AgriTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Connect with fellow farmers or agronomy experts to start exchanging advice.",
                            color = AgriTextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.setTab(AppTab.CONNECTIONS) },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                        ) {
                            Text("Find Farmers to Connect", color = AgriBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(connectedFarmers, key = { it.id }) { partner ->
                        Surface(
                            onClick = { viewModel.setActiveChatPartner(partner) },
                            shape = RoundedCornerShape(12.dp),
                            color = AgriSurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (partner.role == "EXPERT") AgriGreenContainer else AgriSurfaceDark
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (partner.role == "EXPERT") Icons.Default.School else Icons.Default.Person,
                                        contentDescription = null,
                                        tint = if (partner.role == "EXPERT") AgriGreenPrimary else AgriLime
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = partner.fullName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = AgriTextPrimary
                                        )
                                        if (partner.role != "FARMER") {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "• ${partner.role}",
                                                fontSize = 11.sp,
                                                color = AgriLime,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${partner.location} • ${partner.primaryCrops}",
                                        fontSize = 12.sp,
                                        color = AgriTextSecondary
                                    )
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AgriTextSecondary)
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Individual Chat Thread
        val messages by viewModel.currentChatMessages.collectAsState()
        var textInput by remember { mutableStateOf("") }
        val listState = rememberLazyListState()

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AgriBlack)
        ) {
            // Chat Header
            Surface(
                color = AgriSurfaceDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.setActiveChatPartner(null) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AgriGreenPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AgriGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (activePartner!!.role == "EXPERT") Icons.Default.School else Icons.Default.Person,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = activePartner!!.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = AgriTextPrimary
                        )
                        Text(
                            text = "${activePartner!!.role} • ${activePartner!!.location}",
                            fontSize = 11.sp,
                            color = AgriTextSecondary
                        )
                    }
                }
            }

            // Message History
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Start a discussion with ${activePartner!!.fullName} regarding crop management, pest solutions, or farm equipment.",
                                color = AgriTextSecondary,
                                fontSize = 13.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }

                items(messages, key = { it.id }) { msg ->
                    val isMine = msg.senderId == (currentUser?.id ?: 0L)
                    MessageBubble(message = msg, isMine = isMine)
                }
            }

            // Chat Input Bar
            Surface(
                color = AgriSurfaceDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text(AgriStrings.get("send_message", currentLang), fontSize = 13.sp) },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = AgriBorder,
                            focusedTextColor = AgriTextPrimary,
                            unfocusedTextColor = AgriTextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                viewModel.sendMessage(textInput)
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .background(AgriGreenPrimary, CircleShape)
                            .size(46.dp)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = AgriBlack
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageEntity, isMine: Boolean) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (isMine) 14.dp else 2.dp,
                    bottomEnd = if (isMine) 2.dp else 14.dp
                ),
                color = if (isMine) AgriGreenContainer else AgriSurfaceCard,
                border = if (!isMine) androidx.compose.foundation.BorderStroke(1.dp, AgriBorder) else null
            ) {
                Text(
                    text = message.content,
                    color = if (isMine) AgriOnGreenContainer else AgriTextPrimary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
            Text(
                text = formattedTime,
                fontSize = 10.sp,
                color = AgriTextTertiary,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}
