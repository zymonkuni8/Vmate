package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    profile: StudentProfile,
    isDarkMode: Boolean,
    onSaveProfile: (
        fullName: String,
        notificationsEnabled: Boolean,
        streakNotifications: Boolean,
        assignmentNotifications: Boolean
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var fullNameInput by remember { mutableStateOf(profile.fullName) }
    var notificationsEnabled by remember { mutableStateOf(profile.notificationsEnabled) }
    var streakNotifications by remember { mutableStateOf(profile.streakNotifications) }
    var assignmentNotifications by remember { mutableStateOf(profile.assignmentNotifications) }

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)
    val lockedBg = if (isDarkMode) Color(0xFF161A34) else Color(0xFFF1F5F9)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .testTag("edit_profile_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clayEffect(
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = bgColor,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 14.dp
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(GradientPurpleBlue)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Edit Student Profile",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Preferences & Identity Information",
                                fontSize = 11.sp,
                                color = Lavender500
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Editable: Full Name
                Text("Full Name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = fullNameInput,
                    onValueChange = { fullNameInput = it },
                    placeholder = { Text("Your full name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(fieldBg),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = fieldBg,
                        unfocusedContainerColor = fieldBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Locked Academic Verification Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Indigo500.copy(alpha = 0.08f))
                        .border(1.dp, Indigo500.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Indigo500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Verified School Enrollment",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo500
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "This information may require school verification to prevent unauthorized changes.",
                                fontSize = 11.sp,
                                color = textSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Locked School, Class, Section, Roll No fields
                LockedFieldRow("School", profile.schoolName, isDarkMode)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        LockedFieldRow("Class", profile.className, isDarkMode)
                    }
                    Box(modifier = Modifier.weight(0.7f)) {
                        LockedFieldRow("Section", profile.section, isDarkMode)
                    }
                    Box(modifier = Modifier.weight(0.7f)) {
                        LockedFieldRow("Roll No", profile.rollNumber, isDarkMode)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notification Preferences
                Text("Notification Preferences", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                NotificationToggleRow(
                    title = "Push Notifications",
                    subtitle = "Receive timely learning reminders",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(6.dp))

                NotificationToggleRow(
                    title = "Streak Expiration Alerts",
                    subtitle = "Daily nudge to maintain your streak",
                    checked = streakNotifications,
                    onCheckedChange = { streakNotifications = it },
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(6.dp))

                NotificationToggleRow(
                    title = "New Assignment Notices",
                    subtitle = "Alerts when teachers post classwork",
                    checked = assignmentNotifications,
                    onCheckedChange = { assignmentNotifications = it },
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Save button
                Button(
                    onClick = {
                        onSaveProfile(
                            fullNameInput.ifBlank { profile.fullName },
                            notificationsEnabled,
                            streakNotifications,
                            assignmentNotifications
                        )
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_profile_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo500,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Save Profile & Preferences",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LockedFieldRow(
    label: String,
    value: String,
    isDarkMode: Boolean
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val bg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF1F5F9)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textSecondary)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(bg)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = textSecondary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun NotificationToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkMode: Boolean
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val bg = if (isDarkMode) Color(0xFF171B33) else Color(0xFFF8FAFC)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                Text(subtitle, fontSize = 10.sp, color = textSecondary)
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Indigo500
                )
            )
        }
    }
}
