package com.example.ui.screens.progress

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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogItem
import com.example.data.model.StreakState
import com.example.data.model.StudentProfile
import com.example.data.model.SubmittedWork
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientCyanBlue
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.GradientSunset
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary

@Composable
fun StudentProgressScreen(
    profile: StudentProfile,
    streakState: StreakState,
    submittedWorks: List<SubmittedWork>,
    activityLogs: List<ActivityLogItem>,
    isDarkMode: Boolean,
    onTakeQuizAgain: (SubmittedWork) -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("student_progress_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 1. Overall Orbit Level & XP Banner
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(30.dp),
                testTag = "progress_hero_card"
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
                        Column {
                            Text(
                                text = "Learning Orbit Level 4",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "${profile.points} Total Verified XP",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Lavender500
                            )
                        }

                        CuteClayIcon(
                            icon = Icons.Default.Star,
                            contentDescription = "XP Level",
                            gradientColors = listOf(Color(0xFFFBBF24), Color(0xFFD97706)),
                            size = 46.dp,
                            iconSize = 24.dp,
                            isDarkMode = isDarkMode
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Level progress bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Level 4", fontSize = 11.sp, color = textSecondary)
                        Text("350 XP to Level 5", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Indigo500)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (isDarkMode) Color(0xFF13172E) else Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Brush.horizontalGradient(GradientPurpleBlue))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 2. Streak & Protection Status Card
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(26.dp)
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
                            icon = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            gradientColors = listOf(Color(0xFFF97316), Color(0xFFFB923C)),
                            size = 44.dp,
                            iconSize = 24.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "🔥 ${streakState.currentStreak} Day Learning Streak",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Verified daily activity recorded",
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                        }
                    }

                    // Grace freeze recovery rule pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF0EA5E9).copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${streakState.graceFreezesLeft} Freezes Left",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0EA5E9)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 3. Weekly Hours & Accuracy Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgressMiniStatCard(
                    title = "Weekly Study Time",
                    value = "8.5 Hours",
                    subtext = "+1.8 hrs vs last week",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )

                ProgressMiniStatCard(
                    title = "Quiz Accuracy",
                    value = "94%",
                    subtext = "12 quizzes aced",
                    icon = Icons.Default.Quiz,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // 4. Submitted School Work Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Submitted School Work (${submittedWorks.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (submittedWorks.isEmpty()) {
            item {
                Text(
                    text = "No school work uploaded yet. Tap 'Submit Work' on the home dashboard to earn +50 XP and generate AI quizzes!",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }
        } else {
            items(submittedWorks) { work ->
                ClaySurfaceCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    isDarkMode = isDarkMode,
                    shape = RoundedCornerShape(22.dp),
                    testTag = "submitted_work_item_${work.id}"
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = work.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${work.subject} • ${work.workType} • ${work.submissionDateStr}",
                                    fontSize = 11.sp,
                                    color = textSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Indigo500.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "+${work.pointsEarned} XP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Indigo500
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "AI Review: ${work.score}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }
        }

        // 5. Recent Activity & Points History
        item {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Verified Activities",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(activityLogs.take(5)) { log ->
            ClaySurfaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Indigo500.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (log.category) {
                                    "Submission" -> Icons.AutoMirrored.Filled.Assignment
                                    "Quiz" -> Icons.Default.Quiz
                                    "Streak" -> Icons.Default.LocalFireDepartment
                                    else -> Icons.Default.AutoAwesome
                                },
                                contentDescription = null,
                                tint = Indigo500,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = log.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary
                            )
                            Text(
                                text = log.dateFormatted,
                                fontSize = 10.sp,
                                color = textSecondary
                            )
                        }
                    }

                    Text(
                        text = "+${log.pointsAwarded} XP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun ProgressMiniStatCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    ClaySurfaceCard(
        modifier = modifier.height(105.dp),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = if (isDarkMode) DarkTextSecondary else LightTextSecondary)
                Icon(icon, contentDescription = null, tint = Indigo500, modifier = Modifier.size(16.dp))
            }

            Column {
                Text(
                    text = value,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DarkTextPrimary else LightTextPrimary
                )
                Text(
                    text = subtext,
                    fontSize = 10.sp,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary
                )
            }
        }
    }
}
