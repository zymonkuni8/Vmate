package com.example.ui.screens.other

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.CuteClayIcon
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientCyanBlue
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.GradientSunset
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@Composable
fun StaffLearningHubScreen(
    isDarkMode: Boolean,
    onNavigateToAi: (String?) -> Unit,
    onOpenStaffGateway: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("staff_learning_hub_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Staff Hub Hero
        item {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(30.dp),
                testTag = "staff_hub_hero_card"
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
                                icon = Icons.Default.School,
                                contentDescription = "Staff Hub",
                                gradientColors = GradientCyanBlue,
                                size = 46.dp,
                                iconSize = 24.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Staff & Educator Learning Hub",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "AI Pedagogy & Curriculum Support",
                                    fontSize = 11.sp,
                                    color = Lavender500
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Welcome to the school staff learning portal. Explore AI curriculum design, lesson outlines, and differentiated pedagogy resources.",
                        fontSize = 12.sp,
                        color = textSecondary,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Discrete Authorized Administration Gateway
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clayEffect(
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF),
                        borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x606366F1),
                        elevation = 4.dp
                    )
                    .clickable { onOpenStaffGateway() }
                    .padding(18.dp)
                    .testTag("open_staff_gateway_tile")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CuteClayIcon(
                            icon = Icons.Default.Lock,
                            contentDescription = "Staff Gate",
                            gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                            size = 40.dp,
                            iconSize = 20.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Authorized Staff Administration Gate",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Teacher & Principal credential verification",
                                fontSize = 11.sp,
                                color = Lavender500
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
        }

        // Staff Learning Tools Section
        item {
            Text(
                text = "Staff AI Pedagogy Tools",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            val staffTools = listOf(
                Pair("45-Min Differentiated Lesson Plan", "Generate a structured lesson with interactive exit tickets"),
                Pair("Formative Assessment Rubrics", "Create standard-aligned rubrics for high school STEM"),
                Pair("Socratic Questioning Bank", "Inquiry-based prompts to boost student engagement"),
                Pair("Curriculum Mastery Alignment", "Compare syllabus progress against district benchmark")
            )

            staffTools.forEach { (toolTitle, toolDesc) ->
                ClaySurfaceCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    isDarkMode = isDarkMode,
                    shape = RoundedCornerShape(20.dp),
                    onClick = { onNavigateToAi("Generate $toolTitle for high school classes") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CuteClayIcon(
                            icon = Icons.Default.AutoAwesome,
                            contentDescription = toolTitle,
                            gradientColors = GradientSunset,
                            size = 38.dp,
                            iconSize = 18.dp,
                            isDarkMode = isDarkMode
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(toolTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text(toolDesc, fontSize = 11.sp, color = textSecondary)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
