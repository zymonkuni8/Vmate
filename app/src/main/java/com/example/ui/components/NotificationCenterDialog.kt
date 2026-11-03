package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterDialog(
    notifications: List<NotificationItem>,
    isDarkMode: Boolean,
    onMarkAsRead: (String) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onClearAll: () -> Unit,
    onActionClick: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    var selectedFilter by remember { mutableStateOf("All") }
    val categories = listOf("All", "Streak", "Assignment", "Learning", "School", "Achievement")

    val filteredNotifications = remember(notifications, selectedFilter) {
        if (selectedFilter == "All") notifications
        else notifications.filter { it.category.equals(selectedFilter, ignoreCase = true) }
    }

    val unreadCount = remember(notifications) { notifications.count { !it.isRead } }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("notification_center_dialog")
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
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Notifications",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                if (unreadCount > 0) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF97316))
                                            .padding(horizontal = 7.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "$unreadCount new",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Real updates & verified school alerts",
                                fontSize = 11.sp,
                                color = Lavender500
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Mark all as read & Clear
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onMarkAllAsRead)
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, tint = Indigo500, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark all read", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Indigo500)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onClearAll)
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = textSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear all", fontSize = 11.sp, color = textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedFilter == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Indigo500 else cardBg)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Indigo500 else if (isDarkMode) Color(0x33818CF8) else Color(0x306366F1),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedFilter = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // List or Empty State
                if (filteredNotifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(cardBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            CuteClayIcon(
                                icon = Icons.Default.DoneAll,
                                contentDescription = "Caught up",
                                gradientColors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                                size = 48.dp,
                                iconSize = 24.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "You're all caught up",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No new notifications in this category. Verified learning alerts will appear here.",
                                fontSize = 11.sp,
                                color = textSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredNotifications, key = { it.id }) { notif ->
                            NotificationCardItem(
                                notification = notif,
                                isDarkMode = isDarkMode,
                                onClick = {
                                    onMarkAsRead(notif.id)
                                    onActionClick(notif.actionRoute)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    notification: NotificationItem,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (!notification.isRead) {
        if (isDarkMode) Color(0xFF22284C) else Color(0xFFF1F5F9)
    } else {
        if (isDarkMode) Color(0xFF171B33) else Color(0xFFFFFFFF)
    }

    val (icon, gradient) = when (notification.category.lowercase()) {
        "streak" -> Icons.Default.LocalFireDepartment to listOf(Color(0xFFFF5722), Color(0xFFF59E0B))
        "assignment" -> Icons.Default.Assignment to listOf(Color(0xFF3B82F6), Color(0xFF6366F1))
        "learning" -> Icons.Default.AutoAwesome to listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))
        "achievement" -> Icons.Default.EmojiEvents to listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
        "security" -> Icons.Default.Security to listOf(Color(0xFFEF4444), Color(0xFFF97316))
        else -> Icons.Default.School to listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(cardBg)
            .border(
                width = if (!notification.isRead) 1.5.dp else 1.dp,
                color = if (!notification.isRead) Indigo500.copy(alpha = 0.6f) else if (isDarkMode) Color(0x22818CF8) else Color(0x306366F1),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 13.sp,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Text(
                        text = notification.timeAgoFormatted,
                        fontSize = 10.sp,
                        color = textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = notification.message,
                    fontSize = 11.sp,
                    color = textSecondary,
                    lineHeight = 15.sp
                )

                if (notification.actionRoute != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap to view →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo500
                    )
                }
            }

            if (!notification.isRead) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF97316))
                )
            }
        }
    }
}
