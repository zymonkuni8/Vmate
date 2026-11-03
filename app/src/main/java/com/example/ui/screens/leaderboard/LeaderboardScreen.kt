package com.example.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
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
import com.example.data.model.LeaderboardEntry
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
fun LeaderboardScreen(
    leaderboard: List<LeaderboardEntry>,
    isDarkMode: Boolean
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Class 11-A Leaderboard",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Weekly Orbit Points & Study Streaks",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                }

                CuteClayIcon(
                    icon = Icons.Default.EmojiEvents,
                    contentDescription = "Leaderboard",
                    gradientColors = listOf(Color(0xFFFBBF24), Color(0xFFD97706)),
                    size = 44.dp,
                    iconSize = 22.dp,
                    isDarkMode = isDarkMode
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Top 3 Podium or Empty State
        if (leaderboard.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ClaySurfaceCard(
                        modifier = Modifier.fillMaxWidth(),
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CuteClayIcon(
                                icon = Icons.Default.EmojiEvents,
                                contentDescription = "No students yet",
                                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                                size = 56.dp,
                                iconSize = 28.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No students yet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your class leaderboard will appear when students join and start learning.",
                                fontSize = 13.sp,
                                color = textSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        } else if (leaderboard.size in 1..2) {
            item {
                ClaySurfaceCard(
                    modifier = Modifier.fillMaxWidth(),
                    isDarkMode = isDarkMode,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Class 11-A Verified Standings",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Real authenticated students with recorded study activities appear below. More classmates will populate as they submit work.",
                            fontSize = 11.sp,
                            color = textSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            item {
                val top3 = leaderboard.take(3)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // 2nd Place
                    PodiumCard(
                        entry = top3[1],
                        place = "2nd",
                        medalColor = Color(0xFF94A3B8),
                        height = 145.dp,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.weight(1f)
                    )

                    // 1st Place
                    PodiumCard(
                        entry = top3[0],
                        place = "1st",
                        medalColor = Color(0xFFFBBF24),
                        height = 175.dp,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.weight(1f)
                    )

                    // 3rd Place
                    PodiumCard(
                        entry = top3[2],
                        place = "3rd",
                        medalColor = Color(0xFFD97706),
                        height = 130.dp,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (leaderboard.isNotEmpty()) {
            item {
                Text(
                    text = "Class Standings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        items(leaderboard) { entry ->
            val isUser = entry.isCurrentUser
            val itemBg = if (isUser) {
                if (isDarkMode) Color(0xFF22284C) else Color(0xFFEEF2FF)
            } else {
                if (isDarkMode) DarkCardClay else LightCardClay
            }

            ClaySurfaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(20.dp),
                testTag = "leaderboard_item_${entry.rank}"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Rank Number pill
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when (entry.rank) {
                                        1 -> Color(0xFFFEF3C7)
                                        2 -> Color(0xFFF1F5F9)
                                        3 -> Color(0xFFFEE2E2)
                                        else -> if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF1F5F9)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${entry.rank}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (entry.rank) {
                                    1 -> Color(0xFFD97706)
                                    2 -> Color(0xFF64748B)
                                    3 -> Color(0xFFDC2626)
                                    else -> textSecondary
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = entry.name,
                                    fontSize = 14.sp,
                                    fontWeight = if (isUser) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = textPrimary
                                )
                                if (isUser) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(Indigo500)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("YOU", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                            Text(
                                text = entry.className,
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                        }
                    }

                    // Score & Streak
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(15.dp))
                            Text(text = "${entry.streak}d", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${entry.points} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo500
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun PodiumCard(
    entry: LeaderboardEntry,
    place: String,
    medalColor: Color,
    height: androidx.compose.ui.unit.Dp,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    ClaySurfaceCard(
        modifier = modifier.height(height),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(medalColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = place,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = medalColor
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = entry.name.split(" ").first(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DarkTextPrimary else LightTextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "${entry.points} XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Indigo500
                )
            }
        }
    }
}
