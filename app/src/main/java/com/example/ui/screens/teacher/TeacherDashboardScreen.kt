package com.example.ui.screens.teacher

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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
import com.example.data.model.StaffSession
import com.example.data.model.StudentRosterEntity
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@Composable
fun TeacherDashboardScreen(
    session: StaffSession,
    assignedClasses: List<ClassEntity>,
    selectedClass: ClassEntity?,
    students: List<StudentRosterEntity>,
    isDarkMode: Boolean,
    onSelectClass: (ClassEntity) -> Unit,
    onAskAiGradeHelper: (studentName: String) -> Unit,
    onSignOutStaff: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("teacher_dashboard_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Teacher Banner Card
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(30.dp),
                testTag = "teacher_header_card"
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
                                icon = Icons.Default.School,
                                contentDescription = "Teacher",
                                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
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
                                    text = "${session.schoolName} • Faculty",
                                    fontSize = 11.sp,
                                    color = Lavender500
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF10B981).copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Authorized Teacher",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Teacher Permissions Boundary Note
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDarkMode) Color(0xFF181C38) else Color(0xFFF1F5F9))
                            .border(1.dp, if (isDarkMode) Color(0x33818CF8) else Color(0x306366F1), RoundedCornerShape(14.dp))
                        .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = textSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Strict Scope: Viewing only classes assigned to you. Administrative settings are restricted.",
                                fontSize = 10.sp,
                                color = textSecondary,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Assigned Classes Selector (Teacher can ONLY see classes assigned to them)
        item {
            Text(
                text = "Assigned Classes (${assignedClasses.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(assignedClasses) { classItem ->
                    val isSelected = selectedClass?.classId == classItem.classId
                    val cardBg = if (isSelected) {
                        if (isDarkMode) Color(0xFF3730A3) else Color(0xFF4F46E5)
                    } else {
                        if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF)
                    }

                    Box(
                        modifier = Modifier
                            .clayEffect(
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = cardBg,
                                borderColor = if (isSelected) Color(0xFFA5B4FC) else if (isDarkMode) Color(0x33818CF8) else Color(0x506366F1),
                                elevation = if (isSelected) 6.dp else 2.dp
                            )
                            .clickable { onSelectClass(classItem) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .testTag("teacher_class_tab_${classItem.classId}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Class,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Indigo500,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = classItem.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else textPrimary
                                )
                                Text(
                                    text = classItem.subject,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFE0E7FF) else textSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Students Roster Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${selectedClass?.name ?: "Assigned Class"} • Student Roster",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "${students.size} enrolled students",
                        fontSize = 11.sp,
                        color = textSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Indigo500.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${selectedClass?.subject ?: "Subject"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo500
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Student Roster Cards
        // Displays: Student Name, Roll Number, Streak, Points, Submission Status, Learning Progress
        items(students) { student ->
            ClaySurfaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(22.dp),
                testTag = "teacher_student_card_${student.studentId}"
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(GradientPurpleBlue)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = student.fullName.take(1),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = student.fullName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "Roll #${student.rollNumber}",
                                    fontSize = 11.sp,
                                    color = textSecondary
                                )
                            }
                        }

                        // Submission Status Badge
                        val isSubmittedToday = student.submissionStatus.contains("Submitted", ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (isSubmittedToday) Color(0xFF10B981).copy(alpha = 0.12f) else Color(0xFFF59E0B).copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSubmittedToday) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isSubmittedToday) Color(0xFF10B981) else Color(0xFFF59E0B),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = student.submissionStatus,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSubmittedToday) Color(0xFF10B981) else Color(0xFFF59E0B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Streaks & Points Pill Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Streak
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkMode) Color(0xFF181C38) else Color(0xFFF8FAFC))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${student.streakDays} Day Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }

                        // Points
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkMode) Color(0xFF181C38) else Color(0xFFF8FAFC))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${student.points} XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Learning Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Learning Progress", fontSize = 11.sp, color = textSecondary)
                        Text("${(student.overallProgress * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo500)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { student.overallProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Indigo500,
                        trackColor = if (isDarkMode) Color(0xFF1E2344) else Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick AI Grading Prompt
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "🤖 AI Socratic Review",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Indigo500,
                            modifier = Modifier
                                .clickable { onAskAiGradeHelper(student.fullName) }
                                .padding(4.dp)
                        )
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
                Text("Exit Teacher Session", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
