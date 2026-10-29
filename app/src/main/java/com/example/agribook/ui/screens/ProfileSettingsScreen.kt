package com.example.agribook.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agribook.data.local.CropDetailEntity
import com.example.agribook.data.local.UserEntity
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AgriBookViewModel
import com.example.agribook.ui.AppTab
import com.example.agribook.ui.components.LanguageSelectionDialog
import com.example.ui.theme.*

enum class ProfileSubTab {
    DETAILS, CROPS, PRIVACY
}

@Composable
fun ProfileSettingsScreen(viewModel: AgriBookViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val farmerCrops by viewModel.farmerCrops.collectAsState()
    val context = LocalContext.current

    var activeSubTab by remember { mutableStateOf(ProfileSubTab.DETAILS) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddCropDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
    ) {
        // Farmer Header Card
        Surface(
            color = AgriSurfaceDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentUser?.role == "ADMIN") AgriGoldContainer
                                else AgriGreenContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (currentUser?.role) {
                                "ADMIN" -> Icons.Default.Shield
                                "EXPERT" -> Icons.Default.School
                                else -> Icons.Default.Agriculture
                            },
                            contentDescription = "Avatar",
                            tint = if (currentUser?.role == "ADMIN") AgriGold else AgriGreenPrimary,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                val user = currentUser
                                val shareText = "🌾 Check out ${user?.fullName ?: "Farmer"}'s farm profile on Agribook!\n" +
                                        "📍 Location: ${user?.district ?: "Local"}, ${user?.state ?: "India"}\n" +
                                        "🌱 Crops: ${user?.farmingInterests ?: "Agriculture"}\n" +
                                        "Connect with local farmers on Agribook:\n" +
                                        "https://ais-pre-do77zlohazs6k3h2gfqtvp-180384412792.asia-east1.run.app"
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                val chooser = Intent.createChooser(sendIntent, "Share Farm Profile via")
                                context.startActivity(chooser)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = AgriGreenPrimary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AgriGreenPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("share_profile_btn")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AgriStrings.get("share_profile", currentLang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        FilledTonalButton(
                            onClick = { showEditProfileDialog = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = AgriSurfaceCard,
                                contentColor = AgriGreenPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("edit_profile_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AgriStrings.get("edit_profile", currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentUser?.fullName ?: "Farmer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = AgriTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (currentUser?.role == "ADMIN") AgriGoldContainer else AgriGreenContainer
                    ) {
                        Text(
                            text = currentUser?.role ?: "FARMER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentUser?.role == "ADMIN") AgriGold else AgriGreenPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "@${currentUser?.username} • ${currentUser?.phoneOrEmail}",
                    fontSize = 12.sp,
                    color = AgriTextSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgriLime, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentUser?.village}, ${currentUser?.district}, ${currentUser?.state}",
                        fontSize = 12.sp,
                        color = AgriLime
                    )
                }
            }
        }

        // Sub Tab Row
        TabRow(
            selectedTabIndex = activeSubTab.ordinal,
            containerColor = AgriSurfaceCard,
            contentColor = AgriGreenPrimary,
            divider = { HorizontalDivider(color = AgriBorder) }
        ) {
            Tab(
                selected = activeSubTab == ProfileSubTab.DETAILS,
                onClick = { activeSubTab = ProfileSubTab.DETAILS },
                text = { Text("Profile & Land", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
            Tab(
                selected = activeSubTab == ProfileSubTab.CROPS,
                onClick = { activeSubTab = ProfileSubTab.CROPS },
                text = { Text("${AgriStrings.get("my_crops", currentLang)} (${farmerCrops.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Eco, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_my_crops")
            )
            Tab(
                selected = activeSubTab == ProfileSubTab.PRIVACY,
                onClick = { activeSubTab = ProfileSubTab.PRIVACY },
                text = { Text(AgriStrings.get("privacy_settings", currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (activeSubTab) {
                ProfileSubTab.DETAILS -> FarmerDetailsTab(currentUser)
                ProfileSubTab.CROPS -> FarmerCropsTab(
                    crops = farmerCrops,
                    currentLang = currentLang,
                    onAddCropClick = { showAddCropDialog = true },
                    onDeleteCrop = { viewModel.deleteCrop(it) }
                )
                ProfileSubTab.PRIVACY -> PrivacyTab(
                    currentUser = currentUser,
                    currentLang = currentLang,
                    viewModel = viewModel,
                    onChangeLanguageClick = { showLanguageDialog = true }
                )
            }
        }
    }

    if (showEditProfileDialog && currentUser != null) {
        EditProfileDialog(
            user = currentUser!!,
            currentLang = currentLang,
            onDismiss = { showEditProfileDialog = false },
            onSave = { fullName, bio, village, mandal, district, state, crops, farmSize, soil, irrigation, interests ->
                viewModel.updateFarmerProfile(
                    fullName = fullName,
                    bio = bio,
                    village = village,
                    mandal = mandal,
                    district = district,
                    state = state,
                    primaryCrops = crops,
                    farmSizeAcres = farmSize,
                    soilType = soil,
                    irrigationSource = irrigation,
                    farmingInterests = interests
                )
                showEditProfileDialog = false
            }
        )
    }

    if (showAddCropDialog) {
        AddCropDialog(
            currentLang = currentLang,
            onDismiss = { showAddCropDialog = false },
            onAdd = { name, variety, season, acres, sowing, stage, soilNotes, yield ->
                viewModel.addCrop(name, variety, season, acres, sowing, stage, soilNotes, yield)
                showAddCropDialog = false
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLang,
            onLanguageSelected = { viewModel.setLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

@Composable
fun FarmerDetailsTab(user: UserEntity?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Farm Acreage and GPS summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Farm Land Size", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("${user?.farmSizeAcres ?: 0.0} Acres", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriLime)
                }
            }
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("GPS Coordinates", fontSize = 11.sp, color = AgriTextSecondary)
                    Text("${user?.latitude ?: 16.24}°, ${user?.longitude ?: 80.64}°", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                }
            }
        }

        // Agronomic characteristics
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Soil & Water Profile", color = AgriLime, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                HorizontalDivider(color = AgriBorder)

                Row {
                    Text("Soil Type: ", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 13.sp)
                    Text(user?.soilType ?: "Black Cotton Soil", color = AgriTextSecondary, fontSize = 13.sp)
                }
                Row {
                    Text("Irrigation: ", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 13.sp)
                    Text(user?.irrigationSource ?: "Canal & Borewell", color = AgriTextSecondary, fontSize = 13.sp)
                }
                Row {
                    Text("Primary Crops: ", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 13.sp)
                    Text(user?.primaryCrops ?: "Paddy, Cotton", color = AgriOnGreenContainer, fontSize = 13.sp)
                }
            }
        }

        // Farming Interests
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Farming Interests & Sustainable Techniques", color = AgriGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                HorizontalDivider(color = AgriBorder)
                Text(
                    text = user?.farmingInterests ?: "Organic farming, bio-fertilizers, SRI method",
                    color = AgriTextPrimary,
                    fontSize = 13.sp
                )
            }
        }

        // Bio
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Farmer Bio & Experience", color = AgriGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                HorizontalDivider(color = AgriBorder)
                Text(
                    text = user?.bio ?: "Practicing sustainable farming.",
                    color = AgriTextSecondary,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun FarmerCropsTab(
    crops: List<CropDetailEntity>,
    currentLang: AppLanguage,
    onAddCropClick: () -> Unit,
    onDeleteCrop: (Long) -> Unit
) {
    Scaffold(
        containerColor = AgriBlack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddCropClick,
                containerColor = AgriGreenPrimary,
                contentColor = AgriBlack,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(AgriStrings.get("add_crop", currentLang), fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("add_crop_fab")
            )
        }
    ) { padding ->
        if (crops.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Grass, contentDescription = null, tint = AgriTextSecondary, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No crop details recorded yet", color = AgriTextPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Add your current and upcoming crops manually to track stages and yield.", color = AgriTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onAddCropClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                    ) {
                        Text(AgriStrings.get("add_crop", currentLang), color = AgriBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(crops, key = { it.id }) { crop ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
                        modifier = Modifier.fillMaxWidth().testTag("crop_card_${crop.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(AgriGreenContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Eco, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(crop.cropName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AgriTextPrimary)
                                        Text("Variety: ${crop.variety.ifBlank { "Traditional" }}", fontSize = 12.sp, color = AgriLime)
                                    }
                                }
                                IconButton(onClick = { onDeleteCrop(crop.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = AgriRedAlert)
                                }
                            }

                            HorizontalDivider(color = AgriBorder, modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Season & Stage", fontSize = 11.sp, color = AgriTextSecondary)
                                    Text("${crop.season} • ${crop.stage}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Area & Yield", fontSize = 11.sp, color = AgriTextSecondary)
                                    Text("${crop.areaAcres} Ac • ${crop.expectedYieldQuintals} Qtl", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriGold)
                                }
                            }

                            if (crop.sowingDate.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Sown: ${crop.sowingDate}", fontSize = 11.sp, color = AgriTextSecondary)
                            }

                            if (crop.soilHealthNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AgriSurfaceDark,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Notes: ${crop.soilHealthNotes}",
                                        fontSize = 11.sp,
                                        color = AgriTextSecondary,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrivacyTab(
    currentUser: UserEntity?,
    currentLang: AppLanguage,
    viewModel: AgriBookViewModel,
    onChangeLanguageClick: () -> Unit
) {
    var whoCanMessage by remember(currentUser) { mutableStateOf(currentUser?.whoCanMessage ?: "ALL") }
    var profileVisibility by remember(currentUser) { mutableStateOf(currentUser?.profileVisibility ?: "PUBLIC") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Language Selector
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = AgriGreenPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Language / భాష", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 15.sp)
                            Text("${currentLang.nativeName} (${currentLang.displayName})", fontSize = 12.sp, color = AgriLime)
                        }
                    }
                    Button(
                        onClick = onChangeLanguageClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenContainer)
                    ) {
                        Text("Change", color = AgriGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Messaging Privacy
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Who Can Message Me:", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = whoCanMessage == "ALL",
                        onClick = {
                            whoCanMessage = "ALL"
                            viewModel.updatePrivacySettings(whoCanMessage, profileVisibility)
                        },
                        label = { Text("All Farmers", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                    )
                    FilterChip(
                        selected = whoCanMessage == "CONNECTIONS",
                        onClick = {
                            whoCanMessage = "CONNECTIONS"
                            viewModel.updatePrivacySettings(whoCanMessage, profileVisibility)
                        },
                        label = { Text("Connected Network Only", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                    )
                }
            }
        }

        // Profile Visibility
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Profile Visibility:", fontWeight = FontWeight.Bold, color = AgriTextPrimary, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = profileVisibility == "PUBLIC",
                        onClick = {
                            profileVisibility = "PUBLIC"
                            viewModel.updatePrivacySettings(whoCanMessage, profileVisibility)
                        },
                        label = { Text("Public Community", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                    )
                    FilterChip(
                        selected = profileVisibility == "CONNECTIONS",
                        onClick = {
                            profileVisibility = "CONNECTIONS"
                            viewModel.updatePrivacySettings(whoCanMessage, profileVisibility)
                        },
                        label = { Text("Connections Only", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                    )
                }
            }
        }

        // Admin Desk
        if (currentUser?.role == "ADMIN") {
            Button(
                onClick = { viewModel.setTab(AppTab.ADMIN) },
                colors = ButtonDefaults.buttonColors(containerColor = AgriGold),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AgriBlack)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AgriStrings.get("admin_moderation", currentLang), color = AgriBlack, fontWeight = FontWeight.Bold)
            }
        }

        // Logout
        OutlinedButton(
            onClick = { viewModel.logout() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AgriRedAlert),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriRedAlert.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("logout_btn")
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = AgriRedAlert)
            Spacer(modifier = Modifier.width(8.dp))
            Text(AgriStrings.get("logout", currentLang), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EditProfileDialog(
    user: UserEntity,
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (
        fullName: String, bio: String, village: String, mandal: String,
        district: String, state: String, crops: String, farmSize: Double,
        soil: String, irrigation: String, interests: String
    ) -> Unit
) {
    var fullName by remember { mutableStateOf(user.fullName) }
    var bio by remember { mutableStateOf(user.bio) }
    var village by remember { mutableStateOf(user.village) }
    var mandal by remember { mutableStateOf(user.mandal) }
    var district by remember { mutableStateOf(user.district) }
    var state by remember { mutableStateOf(user.state) }
    var crops by remember { mutableStateOf(user.primaryCrops) }
    var farmSizeStr by remember { mutableStateOf(user.farmSizeAcres.toString()) }
    var soil by remember { mutableStateOf(user.soilType) }
    var irrigation by remember { mutableStateOf(user.irrigationSource) }
    var interests by remember { mutableStateOf(user.farmingInterests) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Text(AgriStrings.get("edit_profile", currentLang), color = AgriTextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(AgriStrings.get("full_name", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village / Town") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("District") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = mandal,
                        onValueChange = { mandal = it },
                        label = { Text("Mandal / Block") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = { Text("State") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = crops,
                    onValueChange = { crops = it },
                    label = { Text(AgriStrings.get("primary_crops", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = farmSizeStr,
                        onValueChange = { farmSizeStr = it },
                        label = { Text(AgriStrings.get("farm_size", currentLang)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = soil,
                        onValueChange = { soil = it },
                        label = { Text(AgriStrings.get("soil_type", currentLang)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = irrigation,
                    onValueChange = { irrigation = it },
                    label = { Text(AgriStrings.get("irrigation_source", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = interests,
                    onValueChange = { interests = it },
                    label = { Text(AgriStrings.get("farming_interests", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Farmer Bio") },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val acres = farmSizeStr.toDoubleOrNull() ?: user.farmSizeAcres
                    onSave(fullName, bio, village, mandal, district, state, crops, acres, soil, irrigation, interests)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
            ) {
                Text(AgriStrings.get("save_profile", currentLang), color = AgriBlack, fontWeight = FontWeight.Bold)
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
fun AddCropDialog(
    currentLang: AppLanguage,
    onDismiss: () -> Unit,
    onAdd: (
        name: String, variety: String, season: String, acres: Double,
        sowingDate: String, stage: String, soilNotes: String, yield: Double
    ) -> Unit
) {
    var cropName by remember { mutableStateOf("Paddy") }
    var variety by remember { mutableStateOf("") }
    var season by remember { mutableStateOf("Kharif") }
    var areaStr by remember { mutableStateOf("2.5") }
    var sowingDate by remember { mutableStateOf("July 2026") }
    var stage by remember { mutableStateOf("Vegetative") }
    var soilNotes by remember { mutableStateOf("") }
    var yieldStr by remember { mutableStateOf("25.0") }

    val cropOptions = listOf("Paddy", "Cotton", "Chili", "Wheat", "Sugarcane", "Maize", "Groundnut", "Pulses", "Vegetables")
    val seasonOptions = listOf("Kharif", "Rabi", "Zaid / Summer")
    val stageOptions = listOf("Sown", "Vegetative", "Flowering", "Grain Filling", "Harvested")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AgriSurfaceCard,
        title = {
            Text(AgriStrings.get("add_crop", currentLang), color = AgriTextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(AgriStrings.get("crop_name", currentLang), fontSize = 12.sp, color = AgriLime, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cropOptions.forEach { opt ->
                        FilterChip(
                            selected = cropName == opt,
                            onClick = { cropName = opt },
                            label = { Text(opt, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                        )
                    }
                }

                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text(AgriStrings.get("variety", currentLang)) },
                    placeholder = { Text("e.g. BPT 5204 / RCH 659") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(AgriStrings.get("season", currentLang), fontSize = 12.sp, color = AgriLime, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    seasonOptions.forEach { s ->
                        FilterChip(
                            selected = season == s,
                            onClick = { season = s },
                            label = { Text(s, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = areaStr,
                        onValueChange = { areaStr = it },
                        label = { Text("Area (Acres)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = yieldStr,
                        onValueChange = { yieldStr = it },
                        label = { Text("Yield (Quintals)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(AgriStrings.get("growth_stage", currentLang), fontSize = 12.sp, color = AgriLime, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    stageOptions.forEach { st ->
                        FilterChip(
                            selected = stage == st,
                            onClick = { stage = st },
                            label = { Text(st, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AgriGreenPrimary, selectedLabelColor = AgriBlack)
                        )
                    }
                }

                OutlinedTextField(
                    value = sowingDate,
                    onValueChange = { sowingDate = it },
                    label = { Text(AgriStrings.get("sowing_date", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = soilNotes,
                    onValueChange = { soilNotes = it },
                    label = { Text("Fertilizer & Soil Health Notes") },
                    placeholder = { Text("e.g. Applied Jeevamrutham and Neem cake") },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgriGreenPrimary, focusedTextColor = AgriTextPrimary, unfocusedTextColor = AgriTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val acres = areaStr.toDoubleOrNull() ?: 1.0
                    val yield = yieldStr.toDoubleOrNull() ?: 10.0
                    onAdd(cropName, variety, season, acres, sowingDate, stage, soilNotes, yield)
                },
                enabled = cropName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
            ) {
                Text(AgriStrings.get("add_crop", currentLang), color = AgriBlack, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AgriTextSecondary)
            }
        }
    )
}
