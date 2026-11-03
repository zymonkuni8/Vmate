package com.example.ui.screens.academics

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssignmentItem
import com.example.data.model.AssignmentStatus
import com.example.data.model.SubjectCourse
import com.example.data.model.UserRole
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.GradientSunset
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@Composable
fun AcademicsScreen(
    isDarkMode: Boolean,
    activeRole: UserRole,
    subjects: List<SubjectCourse>,
    assignments: List<AssignmentItem>,
    onSubmitAssignment: (String) -> Unit,
    onAskAiForTopic: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Subjects, 1: Assignments

    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("academics_screen")
    ) {
        // Tab Pills (Clay Switcher)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clayEffect(
                        shape = RoundedCornerShape(50),
                        backgroundColor = if (isDarkMode) Color(0xFF161A34) else Color(0xFFFFFFFF),
                        elevation = 4.dp
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // Tab 0: Subjects
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selectedTab == 0) Brush.linearGradient(GradientPurpleBlue)
                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                        )
                        .clickable { selectedTab = 0 }
                        .padding(vertical = 10.dp)
                        .testTag("academics_tab_subjects"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Courses (${subjects.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) Color.White else textSecondary
                    )
                }

                // Tab 1: Assignments
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (selectedTab == 1) Brush.linearGradient(GradientPurpleBlue)
                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                        )
                        .clickable { selectedTab = 1 }
                        .padding(vertical = 10.dp)
                        .testTag("academics_tab_assignments"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Homework & Tests",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) Color.White else textSecondary
                    )
                }
            }
        }

        // List Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (selectedTab == 0) {
                items(subjects) { subject ->
                    SubjectDetailClayCard(
                        subject = subject,
                        isDarkMode = isDarkMode,
                        onAskAi = { onAskAiForTopic("Help me study ${subject.title}: ${subject.nextTopic}") }
                    )
                }
            } else {
                items(assignments) { assignment ->
                    AssignmentClayCard(
                        assignment = assignment,
                        isDarkMode = isDarkMode,
                        activeRole = activeRole,
                        onSubmit = { onSubmitAssignment(assignment.id) },
                        onGetAiHelp = { onAskAiForTopic("How do I solve assignment: ${assignment.title} for ${assignment.subject}?") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectDetailClayCard(
    subject: SubjectCourse,
    isDarkMode: Boolean,
    onAskAi: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    ClaySurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(26.dp),
        testTag = "subject_detail_${subject.id}"
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
                        icon = Icons.Default.School,
                        contentDescription = subject.title,
                        gradientColors = listOf(Color(subject.colorHex), Color(subject.colorHex).copy(alpha = 0.8f)),
                        size = 46.dp,
                        iconSize = 22.dp,
                        isDarkMode = isDarkMode
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = subject.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "${subject.code} • ${subject.room}",
                            fontSize = 12.sp,
                            color = textSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(subject.colorHex).copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${(subject.progressPercent * 100).toInt()}% Done",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(subject.colorHex)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Next Topic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkMode) Color(0xFF13172E) else Color(0xFFF1F5F9))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Current Syllabus Topic:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subject.nextTopic,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Instructor & AI Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instructor: ${subject.instructor}",
                    fontSize = 12.sp,
                    color = textSecondary
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Brush.linearGradient(GradientPurpleBlue))
                        .clickable(onClick = onAskAi)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Study Guide",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignmentClayCard(
    assignment: AssignmentItem,
    isDarkMode: Boolean,
    activeRole: UserRole,
    onSubmit: () -> Unit,
    onGetAiHelp: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    ClaySurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(26.dp),
        testTag = "assignment_item_${assignment.id}"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${assignment.subject} • Due: ${assignment.dueDate}",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }

                // Status Badge
                val (badgeBg, badgeText, badgeColor) = when (assignment.status) {
                    AssignmentStatus.PENDING -> Triple(Color(0xFFFEF3C7), "Pending", Color(0xFFD97706))
                    AssignmentStatus.SUBMITTED -> Triple(Color(0xFFE0E7FF), "Submitted", Indigo500)
                    AssignmentStatus.GRADED -> Triple(Color(0xFFD1FAE5), assignment.grade ?: "Graded", Color(0xFF059669))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            if (assignment.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = assignment.description,
                    fontSize = 13.sp,
                    color = textSecondary,
                    lineHeight = 18.sp
                )
            }

            if (!assignment.aiSummaryFeedback.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Lavender500.copy(alpha = 0.1f))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Tip",
                            tint = Lavender500,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = assignment.aiSummaryFeedback,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = textPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ask AI for help button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (isDarkMode) Color(0xFF22284C) else Color(0xFFEEF2FF))
                        .clickable(onClick = onGetAiHelp)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "✨ VAISHU AI Help",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Indigo500
                    )
                }

                if (assignment.status == AssignmentStatus.PENDING) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = onSubmit,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo500,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = if (activeRole == UserRole.STUDENT) "Submit" else "Review",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
