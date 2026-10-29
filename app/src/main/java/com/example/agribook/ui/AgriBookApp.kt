package com.example.agribook.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.components.AgriBottomNavigation
import com.example.agribook.ui.components.AgriTopBar
import com.example.agribook.ui.components.LanguageSelectionDialog
import com.example.agribook.ui.components.ReportContentDialog
import com.example.agribook.ui.components.ShareAppDialog
import com.example.agribook.ui.screens.*
import com.example.ui.theme.AgriBlack
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriRedAlert

@Composable
fun AgriBookApp(viewModel: AgriBookViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val unreadNotifs by viewModel.unreadNotificationsCount.collectAsState()
    val unreadMsgs by viewModel.unreadMessagesCount.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val reportTarget by viewModel.reportTarget.collectAsState()
    val activeChatPartner by viewModel.activeChatPartner.collectAsState()

    var showLanguagePicker by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(errorMessage!!)
            viewModel.clearFeedback()
        }
    }

    LaunchedEffect(successMessage) {
        if (!successMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(successMessage!!)
            viewModel.clearFeedback()
        }
    }

    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize().background(AgriBlack)) {
            AuthScreen(viewModel = viewModel)
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.systemBarsPadding()
            )
        }
    } else {
        // Handle Back navigation cleanly
        BackHandler(enabled = currentTab != AppTab.FEED || activeChatPartner != null) {
            if (activeChatPartner != null) {
                viewModel.setActiveChatPartner(null)
            } else if (currentTab != AppTab.FEED) {
                viewModel.setTab(AppTab.FEED)
            }
        }

        Scaffold(
            containerColor = AgriBlack,
            topBar = {
                AgriTopBar(
                    currentLanguage = currentLang,
                    currentTab = currentTab,
                    unreadNotifications = unreadNotifs,
                    isAdmin = currentUser?.role == "ADMIN",
                    onLanguageClick = { showLanguagePicker = true },
                    onNotificationsClick = { viewModel.setTab(AppTab.NOTIFICATIONS) },
                    onAdminClick = { viewModel.setTab(AppTab.ADMIN) },
                    onShareClick = { showShareDialog = true }
                )
            },
            bottomBar = {
                AgriBottomNavigation(
                    currentTab = currentTab,
                    unreadMessages = unreadMsgs,
                    unreadNotifications = unreadNotifs,
                    isAdmin = currentUser?.role == "ADMIN",
                    currentLanguage = currentLang,
                    onTabSelected = { viewModel.setTab(it) }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.navigationBarsPadding()
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag("agribook_main_scaffold")
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(AgriBlack)
            ) {
                when (currentTab) {
                    AppTab.FEED -> FeedScreen(viewModel = viewModel)
                    AppTab.CONNECTIONS -> ConnectionsScreen(viewModel = viewModel)
                    AppTab.CHAT_THREAD -> ChatScreen(viewModel = viewModel)
                    AppTab.NOTIFICATIONS -> NotificationsScreen(viewModel = viewModel)
                    AppTab.PROFILE -> ProfileSettingsScreen(viewModel = viewModel)
                    AppTab.ADMIN -> AdminModerationScreen(viewModel = viewModel)
                }
            }
        }

        if (showShareDialog) {
            ShareAppDialog(
                currentLanguage = currentLang,
                onDismiss = { showShareDialog = false },
                onLinkCopied = {
                    showShareDialog = false
                    viewModel.showFeedback(AgriStrings.get("link_copied", currentLang))
                }
            )
        }

        if (showLanguagePicker) {
            LanguageSelectionDialog(
                currentLanguage = currentLang,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onDismiss = { showLanguagePicker = false }
            )
        }

        if (reportTarget != null) {
            ReportContentDialog(
                onDismiss = { viewModel.setReportTarget(null) },
                onSubmit = { reason -> viewModel.submitReport(reason) }
            )
        }
    }
}
