package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.AssignmentItem
import com.example.data.model.DayProgress
import com.example.data.model.StudentProfile
import com.example.data.model.StudyCapsule
import com.example.data.model.SubjectCourse
import com.example.data.model.UserRole
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.components.VerticalMetricCapsule
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
import com.example.ui.theme.clayEffect

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    activeRole: UserRole,
    studentProfile: StudentProfile,
    calendarDays: List<DayProgress>,
    subjects: List<SubjectCourse>,
    assignments: List<AssignmentItem>,
    capsules: List<StudyCapsule>,
    searchQuery: String,
    onDaySelected: (DayProgress) -> Unit,
    onNavigateToAi: (String?) -> Unit,
    onNavigateToLearn: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onOpenSubmitWork: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenStreakCenter: () -> Unit = {},
    onOpenRapidQuestions: () -> Unit = {},
    onOpenLearningLibrary: () -> Unit = {},
    onSelectCapsule: (StudyCapsule) -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    var selectedDayNumber by remember {
        mutableStateOf(calendarDays.firstOrNull { it.isToday }?.dayNumber ?: calendarDays.firstOrNull()?.dayNumber ?: "15")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // 1. Welcome Hero Header Card
        item {
            StudentHeroBanner(
                profile = studentProfile,
                isDarkMode = isDarkMode,
                onAskAi = { onNavigateToAi(null) }
            )
        }

        // 2. Main Stat Cards (4 Main Cards)
        // 🔥 Daily Streak, ⭐ Points, 🏆 Class Rank, 📚 Learning Progress
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Row 1: Streak & Points
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatPillCard(
                        title = "Daily Streak",
                        value = "${studentProfile.streakDays} Days",
                        subtext = "Tap for Daily Streak Center",
                        icon = Icons.Default.LocalFireDepartment,
                        iconColor = Color(0xFFF97316),
                        isDarkMode = isDarkMode,
                        onClick = onOpenStreakCenter,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_streak"
                    )

                    StatPillCard(
                        title = "Points",
                        value = "${studentProfile.points} XP",
                        subtext = "Verified Study XP",
                        icon = Icons.Default.Star,
                        iconColor = Color(0xFFFBBF24),
                        isDarkMode = isDarkMode,
                        onClick = onOpenStreakCenter,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_points"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Row 2: Class Rank & Learning Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatPillCard(
                        title = "Class Rank",
                        value = studentProfile.classRank,
                        subtext = "Tap to view Leaderboard",
                        icon = Icons.Default.EmojiEvents,
                        iconColor = Color(0xFF8B5CF6),
                        isDarkMode = isDarkMode,
                        onClick = onNavigateToLeaderboard,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_rank"
                    )

                    StatPillCard(
                        title = "Learning Progress",
                        value = "${(studentProfile.overallProgress * 100).toInt()}%",
                        subtext = "On track for finals",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        iconColor = Color(0xFF10B981),
                        isDarkMode = isDarkMode,
                        onClick = onNavigateToLearn,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_progress"
                    )
                }
            }
        }

        // 3. Real Calendar Dates Bar
        // (fix thosedates bar ofthemonthuserealdaysandwhen thestudentclick itthey we see there progressof that day)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Study Timeline",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "Tap any day to inspect daily progress & focus time",
                            fontSize = 12.sp,
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
                            text = "October 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Indigo500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(calendarDays) { day ->
                        VerticalMetricCapsule(
                            dayNumber = day.dayNumber,
                            monthText = day.dayOfWeek,
                            isSelected = selectedDayNumber == day.dayNumber,
                            isDarkMode = isDarkMode,
                            onClick = {
                                selectedDayNumber = day.dayNumber
                                onDaySelected(day)
                            }
                        )
                    }
                }
            }
        }

        // 4. Main Actions Grid
        // 🤖 Ask VAISHU, 📚 Learn, 🎓 Tutorial, 📝 Practice, 🧠 Test, 📷 Submit School Work, 🏆 Leaderboard, 📊 My Progress
        item {
            Spacer(modifier = Modifier.height(28.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Core Learning Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                val actionsList = listOf(
                    ActionItem("Ask VAISHU", Icons.Default.AutoAwesome, GradientPurpleBlue) { onNavigateToAi(null) },
                    ActionItem("Rapid ⚡", Icons.Default.Bolt, listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))) { onOpenRapidQuestions() },
                    ActionItem("Submit Work", Icons.Default.CameraAlt, listOf(Color(0xFFF43F5E), Color(0xFFFB7185))) { onOpenSubmitWork() },
                    ActionItem("Streak Center", Icons.Default.LocalFireDepartment, listOf(Color(0xFFFF5722), Color(0xFFF59E0B))) { onOpenStreakCenter() },
                    ActionItem("Learn", Icons.Default.School, GradientCyanBlue) { onNavigateToLearn() },
                    ActionItem("Library 📖", Icons.Default.Folder, listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))) { onOpenLearningLibrary() },
                    ActionItem("Test", Icons.Default.Quiz, listOf(Color(0xFF8B5CF6), Color(0xFFC084FC))) { onOpenQuiz() },
                    ActionItem("Leaderboard", Icons.Default.EmojiEvents, listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))) { onNavigateToLeaderboard() }
                )

                actionsList.chunked(4).forEach { rowActions ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowActions.forEach { action ->
                            Box(modifier = Modifier.weight(1f)) {
                                StudentActionIconTile(
                                    action = action,
                                    isDarkMode = isDarkMode
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Featured Study Capsules
        item {
            Spacer(modifier = Modifier.height(26.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VAISHU Study Capsules",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Explore All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Indigo500,
                        modifier = Modifier.clickable { onNavigateToLearn() }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                capsules.forEach { capsule ->
                    ClaySurfaceCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        isDarkMode = isDarkMode,
                        onClick = { onSelectCapsule(capsule) },
                        testTag = "capsule_item_${capsule.id}"
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CuteClayIcon(
                                icon = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = capsule.title,
                                gradientColors = GradientSunset,
                                size = 46.dp,
                                iconSize = 22.dp,
                                isDarkMode = isDarkMode
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = capsule.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${capsule.subject} • ${capsule.readMinutes} min read",
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
                                    text = capsule.difficulty,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Indigo500
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class ActionItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val gradient: List<Color>,
    val onClick: () -> Unit
)

@Composable
private fun StudentHeroBanner(
    profile: StudentProfile,
    isDarkMode: Boolean,
    onAskAi: () -> Unit
) {
    ClaySurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        isDarkMode = isDarkMode,
        elevation = 8.dp,
        shape = RoundedCornerShape(32.dp),
        testTag = "student_hero_banner"
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
                        text = "Hello, ${profile.fullName.split(" ").first()}!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) DarkTextPrimary else LightTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${profile.className}-${profile.section} • ${profile.schoolName}",
                        fontSize = 12.sp,
                        color = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(GradientPurpleBlue)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Sparkle",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Weekly Curriculum Mastery",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary
                )
                Text(
                    text = "${(profile.overallProgress * 100).toInt()}% Done",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo500
                )
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
                        .fillMaxWidth(profile.overallProgress)
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Brush.horizontalGradient(GradientPurpleBlue))
                )
            }
        }
    }
}

@Composable
private fun StatPillCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    isDarkMode: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    testTag: String
) {
    ClaySurfaceCard(
        modifier = modifier.height(108.dp),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(22.dp),
        onClick = onClick,
        testTag = testTag
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
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) DarkTextSecondary else LightTextSecondary
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = value,
                    fontSize = 18.sp,
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

@Composable
private fun StudentActionIconTile(
    action: ActionItem,
    isDarkMode: Boolean
) {
    ClaySurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(20.dp),
        onClick = action.onClick,
        testTag = "action_tile_${action.title}"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(action.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = action.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) DarkTextPrimary else LightTextPrimary,
                maxLines = 1
            )
        }
    }
}
