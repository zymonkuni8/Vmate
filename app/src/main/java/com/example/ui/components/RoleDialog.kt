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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    activeStaffSession: com.example.data.model.StaffSession?,
    isDarkMode: Boolean,
    onRoleSelected: (UserRole) -> Unit,
    onRequireStaffAuth: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("role_switcher_dialog")
    ) {
        val bgColor = if (isDarkMode) Color(0xFF14172B) else Color(0xFFFAFAFE)
        val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
        val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clayEffect(
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = bgColor,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 12.dp
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select School Role",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Staff & leadership roles require authorized authentication",
                    fontSize = 12.sp,
                    color = textSecondary
                )
                Spacer(modifier = Modifier.height(20.dp))

                UserRole.values().forEach { role ->
                    val isSelected = role == currentRole
                    val itemBg = if (isSelected) {
                        if (isDarkMode) Color(0xFF22284C) else Color(0xFFEEF2FF)
                    } else {
                        if (isDarkMode) DarkCardClay else LightCardClay
                    }

                    val requiresAuth = (role == UserRole.TEACHER && (activeStaffSession == null || activeStaffSession.role != UserRole.TEACHER)) ||
                            (role == UserRole.PRINCIPAL && (activeStaffSession == null || activeStaffSession.role != UserRole.PRINCIPAL))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(20.dp))
                            .background(itemBg, RoundedCornerShape(20.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Indigo500 else Color(0x20818CF8),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                if (requiresAuth) {
                                    onDismiss()
                                    onRequireStaffAuth(role)
                                } else {
                                    onRoleSelected(role)
                                    onDismiss()
                                }
                            }
                            .padding(14.dp)
                            .testTag("role_option_${role.name}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) Brush.linearGradient(GradientPurpleBlue)
                                        else Brush.linearGradient(listOf(Color(0xFF818CF8), Color(0xFFA5B4FC)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (role) {
                                        UserRole.STUDENT -> Icons.Default.School
                                        UserRole.TEACHER -> Icons.Default.SupervisedUserCircle
                                        UserRole.OTHER, UserRole.PRINCIPAL -> Icons.Default.AdminPanelSettings
                                    },
                                    contentDescription = role.displayName,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = role.displayName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimary
                                    )
                                    if (requiresAuth) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "🔐 Auth Req",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Lavender500
                                        )
                                    }
                                }
                                Text(
                                    text = role.subtitle,
                                    fontSize = 11.sp,
                                    color = textSecondary
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Indigo500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
