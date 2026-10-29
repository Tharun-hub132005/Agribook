package com.example.agribook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agribook.localization.AgriStrings
import com.example.agribook.ui.AgriBookViewModel
import com.example.agribook.ui.AppTab
import com.example.ui.theme.*

@Composable
fun AdminModerationScreen(viewModel: AgriBookViewModel) {
    val reports by viewModel.adminReports.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    if (currentUser?.role != "ADMIN") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AgriBlack)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = AgriRedAlert, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Access Restricted", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 18.sp)
                Text("Only certified agricultural officers and administrators can access moderation.", color = AgriTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.setTab(AppTab.FEED) },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Return to Feed", color = AgriBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.setTab(AppTab.PROFILE) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AgriGold)
            }
            Box(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = AgriStrings.get("admin_moderation", currentLang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriGold
                )
                Text(
                    text = "Review reported content and enforce agricultural community integrity",
                    fontSize = 11.sp,
                    color = AgriTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Summary Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriGold.copy(alpha = 0.4f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Open Reports", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("${reports.count { it.status == "OPEN" }}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AgriGold)
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total Processed", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("${reports.size}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No pending moderation reports", color = AgriTextPrimary, fontWeight = FontWeight.Bold)
                    Text("The agricultural community discussions are clean and healthy.", color = AgriTextSecondary, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reports, key = { it.id }) { report ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (report.status == "OPEN") AgriRedAlert.copy(alpha = 0.6f) else AgriBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("report_card_${report.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (report.targetType == "POST") AgriGreenContainer else AgriGoldContainer
                                ) {
                                    Text(
                                        text = "${report.targetType} #${report.targetId}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (report.targetType == "POST") AgriGreenPrimary else AgriGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (report.status == "OPEN") AgriRedContainer else AgriSurfaceDark
                                ) {
                                    Text(
                                        text = report.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (report.status == "OPEN") AgriRedAlert else AgriLime,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Reason: ${report.reason}",
                                color = AgriTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Text(
                                text = "Reported by: ${report.reporterName}",
                                fontSize = 12.sp,
                                color = AgriTextSecondary
                            )

                            if (report.status == "OPEN") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = { viewModel.dismissAdminReport(report.id, report.targetType, report.targetId) }
                                    ) {
                                        Text("Dismiss", color = AgriTextSecondary, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (report.targetType == "POST") {
                                        Button(
                                            onClick = {
                                                viewModel.resolveAdminReport(report.id, "DELETE", report.targetType, report.targetId)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = AgriRedAlert),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Text("Delete Post", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                viewModel.resolveAdminReport(report.id, "BAN", report.targetType, report.targetId)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = AgriRedAlert),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Text("Ban Farmer", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
