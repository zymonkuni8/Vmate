package com.example.ui.components

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.data.model.UserRole
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@Composable
fun TopSchoolHeader(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    activeRole: UserRole,
    searchQuery: String,
    unreadNotificationsCount: Int = 0,
    onSearchChange: (String) -> Unit,
    onToggleDarkMode: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    onRoleClick: () -> Unit
) {
    val searchBg = if (isDarkMode) Color(0xFF13172E) else Color(0xFFFFFFFF).copy(alpha = 0.85f)
    val searchBorder = if (isDarkMode) Color(0x33818CF8) else Color(0x406366F1)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        // Top Icon row (Notification Bell with Badge - Avatar Pill - Dark/Light toggle)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Notification Bell with subtle unread indicator (Requirement 5)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = if (isDarkMode) DarkCardClay else LightCardClay,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        if (isDarkMode) Color(0x33818CF8) else Color(0x60FFFFFF),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = onNotificationsClick)
                    .testTag("notification_bell_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = if (unreadNotificationsCount > 0) Color(0xFFF97316) else if (isDarkMode) Lavender500 else Indigo500,
                    modifier = Modifier.size(24.dp)
                )

                if (unreadNotificationsCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF97316))
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }

            // Center: Claymorphic Avatar Capsule with Role Switcher
            Box(
                modifier = Modifier
                    .testTag("role_switcher_pill")
                    .height(44.dp)
                    .clayEffect(
                        shape = RoundedCornerShape(50),
                        backgroundColor = if (isDarkMode) Color(0xFF171B33) else Color(0xFFFFFFFF),
                        highlightColor = if (isDarkMode) Color(0x22818CF8) else Color(0xFFFFFFFF),
                        shadowColor = if (isDarkMode) Color(0xFF070914) else Color(0xFFCCD4EE),
                        elevation = 4.dp
                    )
                    .clickable(onClick = onRoleClick)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(GradientPurpleBlue)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activeRole.displayName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Role info",
                        tint = Lavender500,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Right: Theme Mode Switcher
            Box(
                modifier = Modifier
                    .testTag("theme_toggle_button")
                    .size(46.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = if (isDarkMode) DarkCardClay else LightCardClay,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        1.dp,
                        if (isDarkMode) Color(0x33818CF8) else Color(0x60FFFFFF),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable(onClick = onToggleDarkMode),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = if (isDarkMode) Color(0xFFFBBF24) else Indigo500,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Claymorphic Search Bar (styled directly like reference image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = Color.Black.copy(alpha = 0.05f)
                )
                .background(searchBg, RoundedCornerShape(22.dp))
                .border(1.dp, searchBorder, RoundedCornerShape(22.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = textSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = when (activeRole) {
                                UserRole.STUDENT -> "Search lessons, formulas, notes..."
                                UserRole.TEACHER -> "Search students, lesson plans, tests..."
                                UserRole.OTHER, UserRole.PRINCIPAL -> "Search school metrics, faculty, alerts..."
                            },
                            color = textSecondary.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary
                    ),
                    singleLine = true
                )
            }
        }
    }
}
