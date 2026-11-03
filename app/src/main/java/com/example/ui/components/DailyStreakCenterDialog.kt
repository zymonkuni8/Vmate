package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.StreakState
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyStreakCenterDialog(
    streakState: StreakState,
    isDarkMode: Boolean,
    onStartDailyChallenge: () -> Unit,
    onDismiss: () -> Unit
) {
    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    val progress = (streakState.todayPoints.toFloat() / streakState.todayGoalPoints.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "streak_progress")

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("daily_streak_center_dialog")
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
                .padding(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header with Close
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
                                .background(Brush.linearGradient(listOf(Color(0xFFFF5722), Color(0xFFF59E0B)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak Flame",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Daily Streak Center",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Verified Study & Habits",
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

                // Hero Streak Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clayEffect(
                            shape = RoundedCornerShape(24.dp),
                            backgroundColor = cardBg,
                            borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x40F97316),
                            elevation = 4.dp
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🔥 ${streakState.currentStreak} Day Streak",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFF97316)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Personal Best: ${streakState.bestStreak} Days",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (streakState.todayTasksCompleted > 0) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (streakState.todayTasksCompleted > 0) "Active Today ✓" else "Pending Today",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (streakState.todayTasksCompleted > 0) Color(0xFF10B981) else Color(0xFFF59E0B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Today's Verified Progress
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Verified Progress",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "${streakState.todayPoints} / ${streakState.todayGoalPoints} XP (${(animatedProgress * 100).toInt()}%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo500
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = Color(0xFFF97316),
                        trackColor = if (isDarkMode) Color(0xFF1E2344) else Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${streakState.todayTasksCompleted} learning activities verified today",
                        fontSize = 10.sp,
                        color = textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Challenge Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (streakState.isDailyChallengeDone) Color(0xFF10B981).copy(alpha = 0.12f) else Indigo500.copy(alpha = 0.12f))
                        .border(1.dp, if (streakState.isDailyChallengeDone) Color(0xFF10B981).copy(alpha = 0.4f) else Indigo500.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (streakState.isDailyChallengeDone) Color(0xFF10B981) else Indigo500),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (streakState.isDailyChallengeDone) Icons.Default.CheckCircle else Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Daily Challenge",
                                    fontSize = 11.sp,
                                    color = textSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = streakState.dailyChallengeTitle,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                            }
                        }

                        if (!streakState.isDailyChallengeDone) {
                            Button(
                                onClick = onStartDailyChallenge,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Indigo500,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.testTag("start_daily_challenge_button")
                            ) {
                                Text("Start ⚡", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recent 7-Day History
                Text(
                    text = "Recent 7-Day Activity History",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    streakState.recent7Days.forEach { day ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (day.isVerifiedActive) Brush.linearGradient(listOf(Color(0xFFFF5722), Color(0xFFF59E0B)))
                                        else Brush.linearGradient(listOf(cardBg, cardBg))
                                    )
                                    .border(
                                        1.dp,
                                        if (day.isVerifiedActive) Color.White.copy(alpha = 0.5f) else Color(0x30818CF8),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (day.isVerifiedActive) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = "•",
                                        fontSize = 14.sp,
                                        color = textSecondary.copy(alpha = 0.4f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = day.dayLabel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (day.isVerifiedActive) textPrimary else textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Meaningful activity notice
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDarkMode) Color(0xFF181C38) else Color(0xFFF1F5F9))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Streaks advance on verified school work submissions, rapid questions, and quizzes. Simply launching the app does not advance your streak.",
                        fontSize = 10.sp,
                        color = textSecondary,
                        lineHeight = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action Button
                Button(
                    onClick = onStartDailyChallenge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("streak_center_action_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF97316),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Solve Rapid Questions ⚡ (+60 XP)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
