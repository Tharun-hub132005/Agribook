package com.example.agribook.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AppTab
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTopBar(
    currentLanguage: AppLanguage,
    currentTab: AppTab,
    unreadNotifications: Int,
    isAdmin: Boolean,
    onLanguageClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAdminClick: () -> Unit,
    onShareClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AgriBlack,
            titleContentColor = AgriGreenPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("app_bar_title")
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AgriGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Agribook Sprout",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AgriStrings.get("app_name", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = AgriGreenPrimary
                    )
                    Text(
                        text = currentLanguage.nativeName,
                        fontSize = 11.sp,
                        color = AgriLime
                    )
                }
            }
        },
        actions = {
            // Language selector button
            FilledTonalButton(
                onClick = onLanguageClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = AgriSurfaceCard,
                    contentColor = AgriLime
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("lang_toggle_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = "Change Language",
                    modifier = Modifier.size(16.dp),
                    tint = AgriLime
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentLanguage.code.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Admin badge button (if user is admin)
            if (isAdmin) {
                IconButton(
                    onClick = onAdminClick,
                    modifier = Modifier.testTag("admin_desk_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Moderation Desk",
                        tint = AgriGold
                    )
                }
            }

            // Share App button
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.testTag("top_bar_share_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Agribook",
                    tint = AgriGreenPrimary
                )
            }

            // Notifications with badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("notifications_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = AgriGreenPrimary,
                                contentColor = AgriBlack
                            ) {
                                Text("$unreadNotifications")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppTab.NOTIFICATIONS) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Alerts",
                        tint = AgriTextPrimary
                    )
                }
            }
        }
    )
}

@Composable
fun AgriBottomNavigation(
    currentTab: AppTab,
    unreadMessages: Int,
    unreadNotifications: Int,
    isAdmin: Boolean,
    currentLanguage: AppLanguage,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = AgriSurfaceDark,
        contentColor = AgriTextPrimary,
        tonalElevation = 8.dp,
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.FEED,
            onClick = { onTabSelected(AppTab.FEED) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.FEED) Icons.Filled.DynamicFeed else Icons.Outlined.DynamicFeed,
                    contentDescription = AgriStrings.get("feed", currentLanguage)
                )
            },
            label = { Text(AgriStrings.get("feed", currentLanguage), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriBlack,
                indicatorColor = AgriGreenPrimary,
                selectedTextColor = AgriGreenPrimary,
                unselectedIconColor = AgriTextSecondary,
                unselectedTextColor = AgriTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_feed")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.CONNECTIONS,
            onClick = { onTabSelected(AppTab.CONNECTIONS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.CONNECTIONS) Icons.Filled.People else Icons.Outlined.People,
                    contentDescription = AgriStrings.get("connections", currentLanguage)
                )
            },
            label = { Text(AgriStrings.get("connections", currentLanguage), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriBlack,
                indicatorColor = AgriGreenPrimary,
                selectedTextColor = AgriGreenPrimary,
                unselectedIconColor = AgriTextSecondary,
                unselectedTextColor = AgriTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_connections")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.CHAT_THREAD,
            onClick = { onTabSelected(AppTab.CHAT_THREAD) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadMessages > 0) {
                            Badge(containerColor = AgriGreenPrimary, contentColor = AgriBlack) {
                                Text("$unreadMessages")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppTab.CHAT_THREAD) Icons.AutoMirrored.Filled.Chat else Icons.Outlined.ChatBubbleOutline,
                        contentDescription = AgriStrings.get("messages", currentLanguage)
                    )
                }
            },
            label = { Text(AgriStrings.get("messages", currentLanguage), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriBlack,
                indicatorColor = AgriGreenPrimary,
                selectedTextColor = AgriGreenPrimary,
                unselectedIconColor = AgriTextSecondary,
                unselectedTextColor = AgriTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_messages")
        )

        NavigationBarItem(
            selected = currentTab == AppTab.PROFILE,
            onClick = { onTabSelected(AppTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppTab.PROFILE) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = AgriStrings.get("profile", currentLanguage)
                )
            },
            label = { Text(AgriStrings.get("profile", currentLanguage), fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AgriBlack,
                indicatorColor = AgriGreenPrimary,
                selectedTextColor = AgriGreenPrimary,
                unselectedIconColor = AgriTextSecondary,
                unselectedTextColor = AgriTextSecondary
            ),
            modifier = Modifier.testTag("nav_tab_profile")
        )
    }
}

@Composable
fun CropCategoryFilterRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "All", "Paddy", "Wheat", "Cotton", "Chili", "Sugarcane", "Organic", "Soil & Fertilizer", "Pest Control"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = AgriSurfaceCard,
                    labelColor = AgriTextSecondary,
                    selectedContainerColor = AgriGreenPrimary,
                    selectedLabelColor = AgriBlack
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (isSelected) AgriGreenPrimary else AgriBorder,
                    enabled = true,
                    selected = isSelected
                ),
                modifier = Modifier.testTag("filter_chip_$category")
            )
        }
    }
}

@Composable
fun ReportContentDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var selectedReason by remember { mutableStateOf("Misleading farming advice") }
    val reasons = listOf(
        "Misleading farming advice",
        "Spam / Commercial solicitation",
        "Harmful chemical recommendation",
        "Harassment / Inappropriate behavior",
        "Other policy violation"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Text("Report to Agri Moderators", color = AgriTextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Please select the issue with this content. Our agricultural advisory committee reviews all reports.",
                    color = AgriTextSecondary,
                    fontSize = 13.sp
                )
                reasons.forEach { reason ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = reason }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(selectedColor = AgriGreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(reason, color = AgriTextPrimary, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = AgriRedAlert)
            ) {
                Text("Submit Report", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AgriTextSecondary)
            }
        }
    )
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Translate, contentDescription = null, tint = AgriGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AgriStrings.get("select_language", currentLanguage), color = AgriTextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Surface(
                        onClick = {
                            onLanguageSelected(lang)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AgriGreenContainer else AgriSurfaceDark,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AgriGreenPrimary) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(lang.nativeName, color = AgriTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(lang.displayName, color = AgriTextSecondary, fontSize = 12.sp)
                            }
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = AgriTextSecondary)
            }
        }
    )
}

@Composable
fun ShareAppDialog(
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onLinkCopied: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val publicUrl = "https://ais-pre-do77zlohazs6k3h2gfqtvp-180384412792.asia-east1.run.app"
    val shareMessage = "🌾 Join Agribook - The Multilingual Farming Social Network!\nConnect with local farmers, share crop advice, track soil health, and connect nearby in your native language:\n$publicUrl"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AgriGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = AgriStrings.get("share_app", currentLanguage),
                    color = AgriTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = AgriStrings.get("invite_friends", currentLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AgriLime
                )
                Text(
                    text = "Share Agribook with your farming community, neighbors, and agricultural groups online.",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AgriSurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Live Public Web Link:",
                            fontSize = 11.sp,
                            color = AgriTextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = publicUrl,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Copy Link Button
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(publicUrl))
                            onLinkCopied()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AgriGreenPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("copy_link_btn")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AgriStrings.get("copy_link", currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Share to WhatsApp / Apps
                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareMessage)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Agribook via")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_apps_btn")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = AgriBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Apps", color = AgriBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = AgriTextSecondary)
            }
        }
    )
}

