package com.example.ui.screens.admin

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.ClassEntity
import com.example.data.model.MasterAdminStats
import com.example.data.model.SchoolConfigEntity
import com.example.data.model.StaffSession
import com.example.data.model.TeacherEntity
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientSunset
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

private enum class AdminTab {
    STATISTICS,
    TEACHERS,
    CLASSES,
    RULES_CONFIG,
    SECURITY_GATEWAY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterAdminDashboardScreen(
    session: StaffSession,
    stats: MasterAdminStats,
    teachers: List<TeacherEntity>,
    classes: List<ClassEntity>,
    schoolConfig: SchoolConfigEntity?,
    isDarkMode: Boolean,
    onCreateTeacher: (fullName: String, email: String, subject: String, assignedClassId: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onRotateSecretKey: (newKey: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onSaveRules: (submissionPoints: Int, quizPoints: Int, aiStudyPoints: Int, minStreakMins: Int, graceDays: Int, onResult: (Boolean, String?) -> Unit) -> Unit,
    onSignOutStaff: () -> Unit
) {
    var activeTab by remember { mutableStateOf(AdminTab.STATISTICS) }
    var showCreateTeacherModal by remember { mutableStateOf(false) }

    // Rules state
    var submissionXp by remember(schoolConfig) { mutableStateOf((schoolConfig?.submissionPoints ?: 50).toString()) }
    var quizXp by remember(schoolConfig) { mutableStateOf((schoolConfig?.quizPoints ?: 150).toString()) }
    var aiStudyXp by remember(schoolConfig) { mutableStateOf((schoolConfig?.aiStudyPoints ?: 15).toString()) }
    var minStreakMinutes by remember(schoolConfig) { mutableStateOf((schoolConfig?.minStreakMinutes ?: 20).toString()) }
    var rulesSavedMessage by remember { mutableStateOf<String?>(null) }

    // Security Rotation state
    var newSecretKeyInput by remember { mutableStateOf("") }
    var rotationMessage by remember { mutableStateOf<String?>(null) }

    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("master_admin_dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Master Admin Header Banner
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(30.dp),
                testTag = "admin_header_card"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CuteClayIcon(
                                icon = Icons.Default.AdminPanelSettings,
                                contentDescription = "Master Admin",
                                gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFF97316)),
                                size = 46.dp,
                                iconSize = 24.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = session.displayName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "${session.schoolName} • Master Administration",
                                    fontSize = 11.sp,
                                    color = Lavender500
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFF59E0B).copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Principal / Master Admin",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Horizontal Category Tabs
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val tabs = listOf(
                    Pair(AdminTab.STATISTICS, "School Pulse"),
                    Pair(AdminTab.TEACHERS, "Teachers (${teachers.size})"),
                    Pair(AdminTab.CLASSES, "Classes (${classes.size})"),
                    Pair(AdminTab.RULES_CONFIG, "Points & Rules"),
                    Pair(AdminTab.SECURITY_GATEWAY, "Security Rotation")
                )

                items(tabs) { (tab, label) ->
                    val isSelected = activeTab == tab
                    val tabBg = if (isSelected) {
                        if (isDarkMode) Color(0xFF3730A3) else Color(0xFF4F46E5)
                    } else {
                        if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF)
                    }

                    Box(
                        modifier = Modifier
                            .clayEffect(
                                shape = RoundedCornerShape(50),
                                backgroundColor = tabBg,
                                borderColor = if (isSelected) Color(0xFFA5B4FC) else if (isDarkMode) Color(0x33818CF8) else Color(0x506366F1),
                                elevation = if (isSelected) 4.dp else 1.dp
                            )
                            .clickable { activeTab = tab }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("admin_tab_${tab.name}")
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else textPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Section Content
        when (activeTab) {
            // Tab 1: School-wide Statistics
            AdminTab.STATISTICS -> {
                item {
                    Text(
                        text = "School-Level Academic Analytics",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(title = "Total Enrolled", value = "${stats.totalStudents}", subtext = "Students", isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
                        StatTile(title = "Active Faculty", value = "${stats.activeTeachers}", subtext = "Certified Teachers", isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatTile(title = "Average Streak", value = "${stats.averageStreak} Days", subtext = "Habit index", isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
                        StatTile(title = "Submissions Today", value = "${stats.dailySubmissionsCount}", subtext = "Homework verified", isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ClaySurfaceCard(
                        modifier = Modifier.fillMaxWidth(),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("School Attendance Health", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                Text("Real-time campus telemetry", fontSize = 11.sp, color = textSecondary)
                            }
                            Text(stats.schoolAttendanceRate, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981))
                        }
                    }
                }
            }

            // Tab 2: Teacher Management (Create teacher accounts, Generate access codes, Assign classes)
            AdminTab.TEACHERS -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Faculty & Access Codes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )

                        Button(
                            onClick = { showCreateTeacherModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo500, contentColor = Color.White),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("open_create_teacher_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Create Teacher", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(teachers) { teacher ->
                    ClaySurfaceCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Brush.linearGradient(GradientSunset)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(teacher.fullName.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(teacher.fullName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Text(teacher.email, fontSize = 11.sp, color = textSecondary)
                                    }
                                }

                                // Secure Access Code Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Indigo500.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Code: ${teacher.accessCode}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Indigo500
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Department: ${teacher.subject}", fontSize = 11.sp, color = textSecondary)
                                Text("Classes: ${teacher.assignedClassIds}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Lavender500)
                            }
                        }
                    }
                }
            }

            // Tab 3: Classes
            AdminTab.CLASSES -> {
                item {
                    Text("Managed Classes (${classes.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(classes) { classItem ->
                    ClaySurfaceCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Class, contentDescription = null, tint = Indigo500, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(classItem.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text(classItem.subject, fontSize = 11.sp, color = textSecondary)
                                }
                            }
                            Text("ID: ${classItem.classId}", fontSize = 11.sp, color = Lavender500)
                        }
                    }
                }
            }

            // Tab 4: Rules & Points Configurator
            AdminTab.RULES_CONFIG -> {
                item {
                    Text("Points & Streak Rules Configuration", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Master Admin changes are verified and persisted across the entire school.", fontSize = 11.sp, color = textSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    ClaySurfaceCard(
                        modifier = Modifier.fillMaxWidth(),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                            // Submission XP
                            Text("Work Submission Points (XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            TextField(
                                value = submissionXp,
                                onValueChange = { submissionXp = it },
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(fieldBg),
                                colors = TextFieldDefaults.colors(focusedContainerColor = fieldBg, unfocusedContainerColor = fieldBg, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textPrimary, unfocusedTextColor = textPrimary),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quiz Mastery XP
                            Text("Interactive Quiz Points (XP)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            TextField(
                                value = quizXp,
                                onValueChange = { quizXp = it },
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(fieldBg),
                                colors = TextFieldDefaults.colors(focusedContainerColor = fieldBg, unfocusedContainerColor = fieldBg, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textPrimary, unfocusedTextColor = textPrimary),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Daily Streak Min Minutes
                            Text("Minimum Daily Study Minutes for Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            TextField(
                                value = minStreakMinutes,
                                onValueChange = { minStreakMinutes = it },
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(fieldBg),
                                colors = TextFieldDefaults.colors(focusedContainerColor = fieldBg, unfocusedContainerColor = fieldBg, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textPrimary, unfocusedTextColor = textPrimary),
                                singleLine = true
                            )

                            if (rulesSavedMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(rulesSavedMessage ?: "", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val subPoints = submissionXp.toIntOrNull() ?: 50
                                    val qPoints = quizXp.toIntOrNull() ?: 150
                                    val aiPoints = aiStudyXp.toIntOrNull() ?: 15
                                    val streakMins = minStreakMinutes.toIntOrNull() ?: 20
                                    onSaveRules(subPoints, qPoints, aiPoints, streakMins, 2) { success, msg ->
                                        rulesSavedMessage = if (success) "✓ School rules updated successfully!" else msg
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Indigo500, contentColor = Color.White),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Save School Rules", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Tab 5: Security Credential Rotation
            AdminTab.SECURITY_GATEWAY -> {
                item {
                    Text("Secret Credential Rotation", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Rotate the Secret Administration Access Credential. Old credentials will be invalidated immediately.", fontSize = 11.sp, color = textSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    ClaySurfaceCard(
                        modifier = Modifier.fillMaxWidth(),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = Indigo500, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Current Active Gateway Credential", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = schoolConfig?.secretAdminKey ?: "VAISHU-GATEWAY-2026",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Indigo500
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("New Secret Credential", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            TextField(
                                value = newSecretKeyInput,
                                onValueChange = { newSecretKeyInput = it },
                                placeholder = { Text("e.g. VAISHU-STJUDE-2027", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(fieldBg),
                                colors = TextFieldDefaults.colors(focusedContainerColor = fieldBg, unfocusedContainerColor = fieldBg, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textPrimary, unfocusedTextColor = textPrimary),
                                singleLine = true
                            )

                            if (rotationMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(rotationMessage ?: "", fontSize = 11.sp, color = if (rotationMessage?.startsWith("✓") == true) Color(0xFF10B981) else Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (newSecretKeyInput.length < 6) {
                                        rotationMessage = "Credential must be at least 6 characters long."
                                        return@Button
                                    }
                                    onRotateSecretKey(newSecretKeyInput) { success, msg ->
                                        if (success) {
                                            rotationMessage = "✓ Secret Administration Credential rotated successfully!"
                                            newSecretKeyInput = ""
                                        } else {
                                            rotationMessage = msg ?: "Failed to rotate credential."
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(46.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.White),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Rotate Credential Now", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onSignOutStaff,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color(0xFF261D2D) else Color(0xFFFEE2E2),
                    contentColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Exit Master Admin Session", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // Modal: Create Teacher Account
    if (showCreateTeacherModal) {
        CreateTeacherModal(
            classes = classes,
            isDarkMode = isDarkMode,
            onCreate = { name, email, subject, classId ->
                onCreateTeacher(name, email, subject, classId) { success, _ ->
                    if (success) {
                        showCreateTeacherModal = false
                    }
                }
            },
            onDismiss = { showCreateTeacherModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTeacherModal(
    classes: List<ClassEntity>,
    isDarkMode: Boolean,
    onCreate: (name: String, email: String, subject: String, classId: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var selectedClassId by remember { mutableStateOf(classes.firstOrNull()?.classId ?: "class_11_a") }

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clayEffect(
                    shape = RoundedCornerShape(28.dp),
                    backgroundColor = bgColor,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 12.dp
                )
                .padding(20.dp)
        ) {
            Column {
                Text("Create Teacher Account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text("A secure access code will be generated automatically.", fontSize = 11.sp, color = textSecondary)

                Spacer(modifier = Modifier.height(12.dp))

                TextField(value = name, onValueChange = { name = it }, placeholder = { Text("Full Name (e.g. Dr. Jane Smith)", fontSize = 12.sp) }, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(fieldBg), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = email, onValueChange = { email = it }, placeholder = { Text("School Email (e.g. j.smith@stjude.edu)", fontSize = 12.sp) }, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(fieldBg), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = subject, onValueChange = { subject = it }, placeholder = { Text("Department / Subject", fontSize = 12.sp) }, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(fieldBg), singleLine = true)

                Spacer(modifier = Modifier.height(10.dp))
                Text("Assign Class", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(classes) { cl ->
                        val isSelected = selectedClassId == cl.classId
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Indigo500 else fieldBg)
                                .clickable { selectedClassId = cl.classId }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(cl.name, fontSize = 11.sp, color = if (isSelected) Color.White else textPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            if (name.isNotBlank() && email.isNotBlank()) {
                                onCreate(name, email, subject, selectedClassId)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo500, contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Generate Account", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTile(
    title: String,
    value: String,
    subtext: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    ClaySurfaceCard(
        modifier = modifier,
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(title, fontSize = 11.sp, color = textSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = textPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtext, fontSize = 10.sp, color = Indigo500, fontWeight = FontWeight.SemiBold)
        }
    }
}
