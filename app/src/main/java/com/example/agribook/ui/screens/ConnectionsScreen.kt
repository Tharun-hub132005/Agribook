package com.example.agribook.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.agribook.data.local.UserEntity
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AgriBookViewModel
import com.example.ui.theme.*
import com.google.android.gms.location.LocationServices

@Composable
fun ConnectionsScreen(viewModel: AgriBookViewModel) {
    val context = LocalContext.current
    val farmers by viewModel.farmers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val acceptedConnections by viewModel.acceptedConnections.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val filterNearbyOnly by viewModel.filterNearbyOnly.collectAsState()

    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            locationPermissionGranted = true
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        viewModel.updateFarmerLocation(loc.latitude, loc.longitude)
                    } else {
                        // Fallback to coordinates
                        viewModel.updateFarmerLocation(16.24, 80.64)
                    }
                }
            } catch (e: SecurityException) {
                viewModel.updateFarmerLocation(16.24, 80.64)
            }
        }
    }

    val connectedUserIds = remember(acceptedConnections, currentUser) {
        val uid = currentUser?.id ?: 0L
        acceptedConnections.map {
            if (it.requesterId == uid) it.receiverId else it.requesterId
        }.toSet()
    }

    // Sort farmers by distance if location is available
    val displayedFarmers = remember(farmers, filterNearbyOnly, currentUser) {
        val userLat = currentUser?.latitude ?: 16.24
        val userLng = currentUser?.longitude ?: 80.64

        val list = farmers.map { farmer ->
            val dist = viewModel.calculateDistanceKm(userLat, userLng, farmer.latitude, farmer.longitude)
            Pair(farmer, dist)
        }.sortedBy { it.second }

        if (filterNearbyOnly) {
            list.filter { it.second <= 50.0 }.map { it.first }
        } else {
            list.map { it.first }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .padding(16.dp)
    ) {
        // Search bar for friends and fellow farmers
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text(AgriStrings.get("search_hint", currentLang), fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = AgriGreenPrimary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = AgriTextSecondary)
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("farmer_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Location Detection & Nearby Filter Bar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AgriSurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "GPS Location",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "GPS: ${currentUser?.village ?: "Local"}, ${currentUser?.district ?: "Guntur"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriTextPrimary
                        )
                        Text(
                            text = "${currentUser?.latitude ?: 16.24}°, ${currentUser?.longitude ?: 80.64}°",
                            fontSize = 10.sp,
                            color = AgriTextSecondary
                        )
                    }
                }

                FilledTonalButton(
                    onClick = {
                        if (!locationPermissionGranted) {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        } else {
                            try {
                                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                                    if (loc != null) {
                                        viewModel.updateFarmerLocation(loc.latitude, loc.longitude)
                                    } else {
                                        viewModel.updateFarmerLocation(16.24, 80.64)
                                    }
                                }
                            } catch (e: SecurityException) {
                                viewModel.updateFarmerLocation(16.24, 80.64)
                            }
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = AgriGreenContainer,
                        contentColor = AgriGreenPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = AgriStrings.get("detect_location", currentLang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Toggle Chips: All Farmers vs Nearby (<50km)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !filterNearbyOnly,
                onClick = { viewModel.setFilterNearbyOnly(false) },
                label = { Text(AgriStrings.get("all_farmers", currentLang), fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AgriGreenPrimary,
                    selectedLabelColor = AgriBlack
                ),
                leadingIcon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )

            FilterChip(
                selected = filterNearbyOnly,
                onClick = { viewModel.setFilterNearbyOnly(true) },
                label = { Text(AgriStrings.get("filter_nearby", currentLang), fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AgriGreenPrimary,
                    selectedLabelColor = AgriBlack
                ),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Incoming requests
            if (pendingRequests.isNotEmpty()) {
                item {
                    Text(
                        text = "Pending Connection Requests (${pendingRequests.size})",
                        color = AgriGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                items(pendingRequests, key = { "req_${it.id}" }) { req ->
                    val requester = farmers.find { it.id == req.requesterId }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriGold.copy(alpha = 0.5f)),
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AgriGoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = AgriGold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = requester?.fullName ?: "Farmer #${req.requesterId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = AgriTextPrimary
                                )
                                Text(
                                    text = requester?.location ?: "Agricultural Network",
                                    fontSize = 11.sp,
                                    color = AgriTextSecondary
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { viewModel.respondToConnection(req.id, true, req.requesterId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(AgriStrings.get("accept", currentLang), color = AgriBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.respondToConnection(req.id, false, req.requesterId) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(AgriStrings.get("reject", currentLang), color = AgriRedAlert, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Farmer Directory header
            item {
                Text(
                    text = if (filterNearbyOnly) "Nearby Farmers & Crop Peers (<50 km)" else if (searchQuery.isBlank()) "Agricultural Community Directory" else "Search Results",
                    color = AgriLime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (displayedFarmers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LocationOff, contentDescription = null, tint = AgriTextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (filterNearbyOnly) "No farmers found within 50 km." else "No farmers found matching '$searchQuery'",
                                color = AgriTextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else {
                items(displayedFarmers, key = { it.id }) { farmer ->
                    val isConnected = connectedUserIds.contains(farmer.id)
                    val userLat = currentUser?.latitude ?: 16.24
                    val userLng = currentUser?.longitude ?: 80.64
                    val distanceKm = viewModel.calculateDistanceKm(userLat, userLng, farmer.latitude, farmer.longitude)

                    FarmerCard(
                        farmer = farmer,
                        isConnected = isConnected,
                        distanceKm = distanceKm,
                        currentLang = currentLang,
                        onConnect = { viewModel.sendConnectionRequest(farmer) },
                        onChat = { viewModel.setActiveChatPartner(farmer) },
                        onBlock = { viewModel.blockUser(farmer.id, farmer.fullName) },
                        onReport = { viewModel.setReportTarget(Pair("USER", farmer.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun FarmerCard(
    farmer: UserEntity,
    isConnected: Boolean,
    distanceKm: Double,
    currentLang: AppLanguage,
    onConnect: () -> Unit,
    onChat: () -> Unit,
    onBlock: () -> Unit,
    onReport: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AgriSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, AgriBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("farmer_card_${farmer.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            when (farmer.role) {
                                "ADMIN" -> AgriGoldContainer
                                "EXPERT" -> AgriGreenContainer
                                else -> AgriSurfaceDark
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (farmer.role) {
                            "ADMIN" -> Icons.Default.Shield
                            "EXPERT" -> Icons.Default.School
                            else -> Icons.Default.Agriculture
                        },
                        contentDescription = null,
                        tint = when (farmer.role) {
                            "ADMIN" -> AgriGold
                            "EXPERT" -> AgriGreenPrimary
                            else -> AgriLime
                        }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = farmer.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = AgriTextPrimary
                        )
                        if (farmer.role != "FARMER") {
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (farmer.role == "ADMIN") AgriGoldContainer else AgriGreenContainer
                            ) {
                                Text(
                                    text = farmer.role,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (farmer.role == "ADMIN") AgriGold else AgriGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Location & Distance Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "${farmer.location} • ${farmer.farmSizeAcres} Ac",
                            fontSize = 11.sp,
                            color = AgriTextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (distanceKm <= 50.0) AgriGreenContainer else AgriSurfaceDark,
                        modifier = Modifier.padding(top = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (distanceKm <= 50.0) AgriGreenPrimary else AgriTextTertiary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "$distanceKm ${AgriStrings.get("distance_away", currentLang)}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (distanceKm <= 50.0) AgriOnGreenContainer else AgriTextTertiary
                            )
                        }
                    }
                }

                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = AgriTextSecondary)
                    }
                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false },
                        containerColor = AgriSurfaceCardElevated
                    ) {
                        DropdownMenuItem(
                            text = { Text(AgriStrings.get("block", currentLang), color = AgriTextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Block, contentDescription = null, tint = AgriTextSecondary) },
                            onClick = {
                                menuOpen = false
                                onBlock()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(AgriStrings.get("report", currentLang), color = AgriRedAlert) },
                            leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null, tint = AgriRedAlert) },
                            onClick = {
                                menuOpen = false
                                onReport()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Crops and Interests
            Text(
                text = "Crops: ${farmer.primaryCrops}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgriOnGreenContainer
            )

            if (farmer.soilType.isNotBlank()) {
                Text(
                    text = "Soil: ${farmer.soilType} • Irrigation: ${farmer.irrigationSource}",
                    fontSize = 11.sp,
                    color = AgriTextSecondary
                )
            }

            if (farmer.bio.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = farmer.bio,
                    fontSize = 11.sp,
                    color = AgriTextSecondary,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isConnected) {
                    FilledTonalButton(
                        onClick = onChat,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = AgriGreenContainer,
                            contentColor = AgriGreenPrimary
                        ),
                        modifier = Modifier.testTag("chat_with_${farmer.id}")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AgriStrings.get("messages", currentLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onConnect,
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        modifier = Modifier.testTag("connect_with_${farmer.id}")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = AgriBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AgriStrings.get("connect", currentLang), color = AgriBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
