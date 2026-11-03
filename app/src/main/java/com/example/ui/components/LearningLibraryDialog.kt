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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.LearningTopicRecord
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientCyanBlue
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningLibraryDialog(
    topics: List<LearningTopicRecord>,
    isDarkMode: Boolean,
    onLearnAgain: (topic: String, subject: String) -> Unit,
    onNewRapidQuestions: (topic: String, subject: String) -> Unit,
    onPracticeQuestions: (topic: String, subject: String) -> Unit,
    onTakeTest: (topic: String, subject: String) -> Unit,
    onDismiss: () -> Unit
) {
    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .testTag("learning_library_dialog")
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
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(GradientCyanBlue)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalLibrary,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Learning Library",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Your studied topics & question history",
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

                if (topics.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(cardBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            CuteClayIcon(
                                icon = Icons.Default.MenuBook,
                                contentDescription = "Empty library",
                                gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                                size = 52.dp,
                                iconSize = 26.dp,
                                isDarkMode = isDarkMode
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "You haven't studied any topics yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Start learning, complete Rapid Questions, or submit school work and your topics will appear here.",
                                fontSize = 12.sp,
                                color = textSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                lineHeight = 17.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topics, key = { it.id }) { item ->
                            LibraryTopicCard(
                                topic = item,
                                isDarkMode = isDarkMode,
                                onLearnAgain = { onLearnAgain(item.topic, item.subject) },
                                onNewRapidQuestions = { onNewRapidQuestions(item.topic, item.subject) },
                                onPracticeQuestions = { onPracticeQuestions(item.topic, item.subject) },
                                onTakeTest = { onTakeTest(item.topic, item.subject) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryTopicCard(
    topic: LearningTopicRecord,
    isDarkMode: Boolean,
    onLearnAgain: () -> Unit,
    onNewRapidQuestions: () -> Unit,
    onPracticeQuestions: () -> Unit,
    onTakeTest: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color(0xFF171B33) else Color(0xFFF8FAFC)

    var expandedActions by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg)
            .border(
                width = 1.dp,
                color = if (isDarkMode) Color(0x33818CF8) else Color(0x306366F1),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topic.subject.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo500
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = topic.topic,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    if (topic.chapter.isNotBlank()) {
                        Text(
                            text = topic.chapter,
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Indigo500.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${topic.accuracyPercentage}% Accuracy",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo500
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { topic.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(50)),
                color = Indigo500,
                trackColor = if (isDarkMode) Color(0xFF262D55) else Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stats row: Questions completed, Sessions, Last studied
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📝 ${topic.questionsCompleted} questions • ${topic.sessionsCount} sessions",
                    fontSize = 10.sp,
                    color = textSecondary
                )
                Text(
                    text = "Studied: ${topic.lastStudiedFormatted}",
                    fontSize = 10.sp,
                    color = textSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // New Questions (Rapid)
                Button(
                    onClick = onNewRapidQuestions,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo500,
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("New Questions", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Learn Again (AI)
                OutlinedButton(
                    onClick = onLearnAgain,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp), tint = Lavender500)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Learn Again", fontSize = 10.sp)
                }

                // Practice
                OutlinedButton(
                    onClick = onPracticeQuestions,
                    modifier = Modifier
                        .weight(0.9f)
                        .height(34.dp),
                    shape = RoundedCornerShape(50),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp)
                ) {
                    Text("Practice", fontSize = 10.sp)
                }
            }
        }
    }
}
