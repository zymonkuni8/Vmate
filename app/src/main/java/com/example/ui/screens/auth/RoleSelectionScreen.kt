package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.clayEffect

@Composable
fun RoleSelectionScreen(
    isDarkMode: Boolean,
    onRoleSelected: (UserRole) -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("role_selection_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Your Role",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose your profile type to customize your VAISHU learning environment",
                fontSize = 13.sp,
                color = textSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Primary Option 1: STUDENT
            RoleOptionCard(
                title = "🎓 STUDENT",
                subtitle = "Access personalized AI tutoring, study streaks, quizzes, and class homework helper.",
                badgeText = "Recommended for learners",
                gradient = GradientPurpleBlue,
                icon = Icons.Default.School,
                isDarkMode = isDarkMode,
                onClick = { onRoleSelected(UserRole.STUDENT) },
                testTag = "select_role_student"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Option 2: OTHER
            RoleOptionCard(
                title = "👤 OTHER",
                subtitle = "For school teachers, faculty coordinators, department heads, and school leadership.",
                badgeText = "Educators & School Staff",
                gradient = GradientCyanBlue,
                icon = Icons.Default.Person,
                isDarkMode = isDarkMode,
                onClick = { onRoleSelected(UserRole.OTHER) },
                testTag = "select_role_other"
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "by Zymon",
                fontSize = 12.sp,
                color = textSecondary.copy(alpha = 0.6f),
                letterSpacing = 1.2.sp
            )
        }
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    subtitle: String,
    badgeText: String,
    gradient: List<Color>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    ClaySurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        isDarkMode = isDarkMode,
        shape = RoundedCornerShape(28.dp),
        elevation = 8.dp,
        onClick = onClick,
        testTag = testTag
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
                        icon = icon,
                        contentDescription = title,
                        gradientColors = gradient,
                        size = 48.dp,
                        iconSize = 24.dp,
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) DarkTextPrimary else LightTextPrimary
                        )
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Indigo500
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = if (isDarkMode) DarkTextSecondary else LightTextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}
