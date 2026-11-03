package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.glassEffect
import com.example.ui.viewmodel.AppNavTab

@Composable
fun ClayBottomBar(
    currentTab: AppNavTab,
    isDarkMode: Boolean,
    onTabSelected: (AppNavTab) -> Unit,
    onOpenStreakCenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val barBg = if (isDarkMode) Color(0xD812162C) else Color(0xEAFFFFFF)
    val barBorder = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Floating glassmorphic dock
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .glassEffect(
                    shape = RoundedCornerShape(34.dp),
                    backgroundColor = barBg,
                    borderColor = barBorder,
                    shadowElevation = 10.dp
                )
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home
                NavIconItem(
                    selected = currentTab == AppNavTab.HOME,
                    filledIcon = Icons.Filled.Home,
                    outlinedIcon = Icons.Outlined.Home,
                    label = "Home",
                    contentDescription = "Home",
                    isDarkMode = isDarkMode,
                    onClick = { onTabSelected(AppNavTab.HOME) },
                    testTag = "nav_home_button"
                )

                // 2. Learn
                NavIconItem(
                    selected = currentTab == AppNavTab.LEARN,
                    filledIcon = Icons.AutoMirrored.Filled.MenuBook,
                    outlinedIcon = Icons.AutoMirrored.Outlined.MenuBook,
                    label = "Learn",
                    contentDescription = "Learn",
                    isDarkMode = isDarkMode,
                    onClick = { onTabSelected(AppNavTab.LEARN) },
                    testTag = "nav_learn_button"
                )

                // 3. Center Spacer for Elevated Streak Button
                Box(modifier = Modifier.width(68.dp))

                // 4. Ask VAISHU AI
                NavIconItem(
                    selected = currentTab == AppNavTab.ASK_VAISHU,
                    filledIcon = Icons.Filled.AutoAwesome,
                    outlinedIcon = Icons.Outlined.AutoAwesome,
                    label = "AI Chat",
                    contentDescription = "Ask VAISHU",
                    isDarkMode = isDarkMode,
                    onClick = { onTabSelected(AppNavTab.ASK_VAISHU) },
                    testTag = "nav_ask_vaishu_button"
                )

                // 5. Profile
                NavIconItem(
                    selected = currentTab == AppNavTab.PROFILE,
                    filledIcon = Icons.Filled.Person,
                    outlinedIcon = Icons.Outlined.Person,
                    label = "Profile",
                    contentDescription = "Profile",
                    isDarkMode = isDarkMode,
                    onClick = { onTabSelected(AppNavTab.PROFILE) },
                    testTag = "nav_profile_button"
                )
            }
        }

        // =========================================================================
        // CENTER STREAK BUTTON
        // Dedicated Streak / Daily Challenge button elevated above the navigation.
        // Soft 3D depth, claymorphism, subtle glow, press animation, clean flame icon.
        // =========================================================================
        val streakInteractionSource = remember { MutableInteractionSource() }
        val isPressed by streakInteractionSource.collectIsPressedAsState()
        val streakScale by animateFloatAsState(
            targetValue = if (isPressed) 0.92f else 1.05f,
            label = "streak_scale"
        )

        Box(
            modifier = Modifier
                .offset(y = (-14).dp)
                .scale(streakScale)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = Color(0xFFFF5722).copy(alpha = 0.5f),
                    spotColor = Color(0xFFF59E0B).copy(alpha = 0.6f)
                )
                .clip(RoundedCornerShape(26.dp))
                .background(
                    brush = Brush.linearGradient(listOf(Color(0xFFFF5722), Color(0xFFF59E0B)))
                )
                .border(2.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(26.dp))
                .clickable(
                    interactionSource = streakInteractionSource,
                    indication = null,
                    onClick = onOpenStreakCenter
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .testTag("center_streak_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak Flame",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Streak",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun NavIconItem(
    selected: Boolean,
    filledIcon: androidx.compose.ui.graphics.vector.ImageVector,
    outlinedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    contentDescription: String,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val tintColor by animateColorAsState(
        targetValue = if (selected) Indigo500 else if (isDarkMode) DarkTextSecondary else LightTextSecondary,
        label = "nav_tint"
    )

    Column(
        modifier = Modifier
            .size(width = 54.dp, height = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (selected) filledIcon else outlinedIcon,
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = tintColor
        )
    }
}
