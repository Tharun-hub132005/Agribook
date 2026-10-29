package com.example.agribook.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.ui.AgriBookViewModel
import com.example.agribook.ui.AppTab
import com.example.agribook.ui.AuthScreenState
import com.example.ui.theme.*

@Composable
fun AuthScreen(viewModel: AgriBookViewModel) {
    val authState by viewModel.authScreenState.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    when (authState) {
        AuthScreenState.LOGIN -> LoginView(viewModel, currentLang)
        AuthScreenState.REGISTER -> RegisterView(viewModel, currentLang)
        AuthScreenState.LANGUAGE_PICKER_FIRST_TIME -> FirstTimeLanguagePicker(viewModel, currentLang)
    }
}

@Composable
private fun LoginView(viewModel: AgriBookViewModel, currentLang: AppLanguage) {
    var identifier by remember { mutableStateOf("ramesh@farm.org") }
    var password by remember { mutableStateOf("farmer123") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Hero Header Image / Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.agribook_banner),
                contentDescription = "Agribook Farmland Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(
                                androidx.compose.ui.graphics.Color.Transparent,
                                AgriBlack.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AgriGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = AgriBlack,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = AgriStrings.get("app_name", currentLang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriTextPrimary
                    )
                    Text(
                        text = AgriStrings.get("tagline", currentLang),
                        fontSize = 11.sp,
                        color = AgriLime
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Demo Login Badges for Instant Testing
        Surface(
            color = AgriSurfaceCard,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Quick Demo Accounts:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgriLime
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistChip(
                        onClick = {
                            identifier = "ramesh@farm.org"
                            password = "farmer123"
                        },
                        label = { Text("Ramesh (Farmer)", fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(labelColor = AgriTextPrimary)
                    )
                    AssistChip(
                        onClick = {
                            identifier = "ananya@agri.edu"
                            password = "expert123"
                        },
                        label = { Text("Ananya (Expert)", fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(labelColor = AgriLime)
                    )
                    AssistChip(
                        onClick = {
                            identifier = "admin@agribook.org"
                            password = "admin123"
                        },
                        label = { Text("Admin Desk", fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(labelColor = AgriGold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text(AgriStrings.get("phone_or_email", currentLang)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AgriGreenPrimary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary,
                focusedLabelColor = AgriGreenPrimary,
                unfocusedLabelColor = AgriTextSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_identifier_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(AgriStrings.get("password", currentLang)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AgriGreenPrimary) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password",
                        tint = AgriTextSecondary
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary,
                focusedLabelColor = AgriGreenPrimary,
                unfocusedLabelColor = AgriTextSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login(identifier, password) },
            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("login_submit_btn")
        ) {
            Text(
                text = AgriStrings.get("login", currentLang),
                color = AgriBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { viewModel.setAuthScreenState(AuthScreenState.REGISTER) },
            modifier = Modifier.testTag("goto_register_btn")
        ) {
            Text(
                text = "${AgriStrings.get("register", currentLang)} →",
                color = AgriLime,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RegisterView(viewModel: AgriBookViewModel, currentLang: AppLanguage) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var crops by remember { mutableStateOf("") }
    var farmSizeStr by remember { mutableStateOf("5.0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.setAuthScreenState(AuthScreenState.LOGIN) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AgriGreenPrimary)
            }
            Text(
                text = AgriStrings.get("register", currentLang),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AgriTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(AgriStrings.get("full_name", currentLang)) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_fullname_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username (Unique ID)") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_username_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text(AgriStrings.get("phone_or_email", currentLang)) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_identifier_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(AgriStrings.get("password", currentLang)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_password_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(AgriStrings.get("location", currentLang)) },
            placeholder = { Text("e.g. Warangal, Telangana") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_location_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = crops,
            onValueChange = { crops = it },
            label = { Text(AgriStrings.get("primary_crops", currentLang)) },
            placeholder = { Text("e.g. Cotton, Chili, Paddy") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_crops_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = farmSizeStr,
            onValueChange = { farmSizeStr = it },
            label = { Text(AgriStrings.get("farm_size", currentLang)) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AgriGreenPrimary,
                unfocusedBorderColor = AgriBorder,
                focusedTextColor = AgriTextPrimary,
                unfocusedTextColor = AgriTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_farmsize_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val farmSize = farmSizeStr.toDoubleOrNull() ?: 1.0
                viewModel.register(
                    username = if (username.isBlank()) "farmer_${System.currentTimeMillis() % 10000}" else username,
                    fullName = fullName,
                    identifier = identifier,
                    passwordPlain = password,
                    location = if (location.isBlank()) "India" else location,
                    crops = if (crops.isBlank()) "Mixed Crops" else crops,
                    farmSize = farmSize
                )
            },
            enabled = fullName.isNotBlank() && identifier.isNotBlank() && password.length >= 6,
            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("register_submit_btn")
        ) {
            Text(
                text = AgriStrings.get("register", currentLang),
                color = AgriBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun FirstTimeLanguagePicker(viewModel: AgriBookViewModel, currentLang: AppLanguage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgriBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AgriGreenContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Translate, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to Agribook!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = AgriTextPrimary
        )

        Text(
            text = "Select your preferred farming community language:",
            fontSize = 14.sp,
            color = AgriTextSecondary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppLanguage.entries.forEach { lang ->
            val isSelected = lang == currentLang
            Surface(
                onClick = { viewModel.setLanguage(lang) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) AgriGreenContainer else AgriSurfaceCard,
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, AgriGreenPrimary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(lang.nativeName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AgriTextPrimary)
                        Text(lang.displayName, fontSize = 13.sp, color = AgriTextSecondary)
                    }
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.setAuthScreenState(AuthScreenState.LOGIN)
                viewModel.setTab(AppTab.FEED)
            },
            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("language_continue_btn")
        ) {
            Text(
                text = AgriStrings.get("continue_btn", currentLang),
                color = AgriBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
