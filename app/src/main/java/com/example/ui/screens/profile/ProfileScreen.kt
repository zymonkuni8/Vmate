package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.data.model.UserRole
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientCyanBlue
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    activeRole: UserRole,
    studentProfile: StudentProfile,
    customApiKey: String,
    activeStaffSession: com.example.data.model.StaffSession? = null,
    onOpenStaffGateway: () -> Unit = {},
    onSignOutStaff: () -> Unit = {},
    onOpenLearningLibrary: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    notificationsEnabled: Boolean = true,
    onToggleNotifications: () -> Unit = {},
    onSaveCustomApiKey: (String) -> Unit,
    onResetCustomApiKey: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onChangeRole: () -> Unit,
    onSignOut: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    var keyInput by remember(customApiKey) { mutableStateOf(customApiKey) }
    var isKeyVisible by remember { mutableStateOf(false) }
    var keySavedMessage by remember { mutableStateOf(false) }

    var socraticMode by remember { mutableStateOf(true) }
    var strictCitations by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // User Profile Card
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(32.dp),
                testTag = "user_profile_card"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(GradientPurpleBlue)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = studentProfile.fullName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = studentProfile.email,
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${studentProfile.className}-${studentProfile.section} • Roll #${studentProfile.rollNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Lavender500
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = studentProfile.schoolName,
                        fontSize = 12.sp,
                        color = textSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Indigo500.copy(alpha = 0.15f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${activeRole.displayName} • ${studentProfile.classRank}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Indigo500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ==========================================
        // User Option: Add or Change API Key Setting
        // ==========================================
        item {
            Text(
                text = "Gemini AI API Key Configuration",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(26.dp),
                testTag = "api_key_setting_card"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CuteClayIcon(
                                icon = Icons.Default.Key,
                                contentDescription = "API Key",
                                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                                size = 40.dp,
                                iconSize = 20.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Custom Gemini API Key",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = if (customApiKey.isNotBlank()) "Custom key configured" else "Using default cloud key",
                                    fontSize = 11.sp,
                                    color = if (customApiKey.isNotBlank()) Color(0xFF10B981) else Lavender500,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        if (customApiKey.isNotBlank()) {
                            IconButton(onClick = {
                                onResetCustomApiKey()
                                keyInput = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = "Reset Key",
                                    tint = textSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Enter your Google Gemini API key to customize AI rate limits or quotas. Keys are stored safely on your device.",
                        fontSize = 12.sp,
                        color = textSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Text Field for Key
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDarkMode) Color(0xFF13172E) else Color(0xFFF1F5F9))
                            .border(
                                1.dp,
                                if (isDarkMode) Color(0x33818CF8) else Color(0x506366F1),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = keyInput,
                                onValueChange = {
                                    keyInput = it
                                    keySavedMessage = false
                                },
                                placeholder = {
                                    Text(
                                        text = "AIzaSy...",
                                        fontSize = 13.sp,
                                        color = textSecondary.copy(alpha = 0.5f)
                                    )
                                },
                                visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("custom_api_key_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = textPrimary,
                                    unfocusedTextColor = textPrimary
                                ),
                                singleLine = true
                            )

                            IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
                                Icon(
                                    imageVector = if (isKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Visibility",
                                    tint = textSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (keySavedMessage) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Key Saved Successfully!", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Button(
                            onClick = {
                                onSaveCustomApiKey(keyInput)
                                keySavedMessage = true
                            },
                            enabled = keyInput.isNotBlank() && keyInput != customApiKey,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Indigo500,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("save_api_key_button")
                        ) {
                            Text("Save Key", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ==========================================
        // Secret Staff Administration Gateway
        // ==========================================
        item {
            Text(
                text = "Staff & Administration",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(26.dp),
                onClick = onOpenStaffGateway,
                testTag = "open_staff_gateway_button"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.Lock,
                            contentDescription = "Staff Gate",
                            gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (activeStaffSession != null) "Staff Session Active" else "Staff Administration Gateway",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = if (activeStaffSession != null) "${activeStaffSession.displayName} (${activeStaffSession.role.displayName})" else "Protected portal for verified teachers & principals",
                                fontSize = 11.sp,
                                color = if (activeStaffSession != null) Color(0xFF10B981) else Lavender500,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (activeStaffSession != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Sign Out Staff Session",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444),
                        modifier = Modifier
                            .clickable { onSignOutStaff() }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Account & Role Configuration
        item {
            Text(
                text = "Account & Preferences",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Switch Role
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(24.dp),
                onClick = onChangeRole,
                testTag = "profile_switch_role_item"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.SwitchAccount,
                            contentDescription = "Switch Role",
                            gradientColors = GradientPurpleBlue,
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Current Role",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = activeRole.displayName,
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open role switcher",
                        tint = textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dark Mode Toggle Card
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(24.dp),
                testTag = "profile_dark_mode_item"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.DarkMode,
                            contentDescription = "Dark Theme",
                            gradientColors = listOf(Color(0xFF6366F1), Color(0xFF38BDF8)),
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Dark Mode",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = if (isDarkMode) "Deep Navy & Indigo Glass" else "Frosted White & Soft Lavender",
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Indigo500
                        ),
                        modifier = Modifier.testTag("profile_dark_mode_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Learning Library Card
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(24.dp),
                onClick = onOpenLearningLibrary,
                testTag = "profile_learning_library_item"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.LocalLibrary,
                            contentDescription = "Learning Library",
                            gradientColors = GradientCyanBlue,
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Learning Library",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "View all studied topics, accuracy & notes",
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Learning Library",
                        tint = textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Push & School Alerts Card
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(24.dp),
                testTag = "profile_notifications_item"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOpenNotifications() }
                    ) {
                        CuteClayIcon(
                            icon = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            gradientColors = listOf(Color(0xFFF97316), Color(0xFFFBBF24)),
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Push & School Alerts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = if (notificationsEnabled) "Active • Real school alerts only" else "Muted • Tap to view alerts",
                                fontSize = 12.sp,
                                color = textSecondary
                            )
                        }
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { onToggleNotifications() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Indigo500
                        ),
                        modifier = Modifier.testTag("profile_notifications_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Local Storage & Data Security Card (Requirement 8, 10, 11)
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(24.dp),
                testTag = "profile_local_data_security_item"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.Security,
                            contentDescription = "Data Protection",
                            gradientColors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                            size = 36.dp,
                            iconSize = 18.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Local Storage & Data Security",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Protected local cache • Source of truth: School Server",
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Profile details cached locally for instant startup\n• Official points, streak, and rank are verified server-side\n• Zero sensitive keys stored in plaintext\n• School isolation: St. Jude's International Academy",
                        fontSize = 11.sp,
                        color = textSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Sign Out Button
        item {
            Button(
                onClick = onSignOut,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFF22284C) else Color(0xFFFEE2E2),
                    contentColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("sign_out_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out / Switch Account",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
