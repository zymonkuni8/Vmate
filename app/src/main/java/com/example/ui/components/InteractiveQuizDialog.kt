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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveQuizDialog(
    questions: List<QuizQuestion>,
    isDarkMode: Boolean,
    bannerTitle: String = "Nice! Let's see what you learned today.",
    onCompleteQuiz: (score: Int, total: Int) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions.getOrNull(currentIndex) ?: questions.first()

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("interactive_quiz_dialog")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clayEffect(
                    shape = RoundedCornerShape(32.dp),
                    backgroundColor = bgColor,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 12.dp
                )
                .padding(22.dp)
        ) {
            if (!isQuizCompleted) {
                Column {
                    // Interactive VAISHU Banner
                    if (bannerTitle.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Indigo500.copy(alpha = 0.12f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Indigo500,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = bannerTitle,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Indigo500
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = Indigo500,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Question ${currentIndex + 1} of ${questions.size}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Indigo500
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = currentQ.question,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentQ.options.forEachIndexed { optIndex, optionText ->
                            val isSelected = selectedOptionIndex == optIndex
                            val isCorrect = optIndex == currentQ.correctIndex

                            val (itemBg, itemBorder, itemText) = when {
                                isAnswerSubmitted && isCorrect -> Triple(Color(0xFFD1FAE5), Color(0xFF10B981), Color(0xFF065F46))
                                isAnswerSubmitted && isSelected && !isCorrect -> Triple(Color(0xFFFEE2E2), Color(0xFFEF4444), Color(0xFF991B1B))
                                isSelected -> Triple(Indigo500.copy(alpha = 0.15f), Indigo500, Indigo500)
                                else -> Triple(if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC), Color.Transparent, textPrimary)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(itemBg)
                                    .border(1.5.dp, itemBorder, RoundedCornerShape(16.dp))
                                    .clickable(enabled = !isAnswerSubmitted) {
                                        selectedOptionIndex = optIndex
                                    }
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = optionText,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected || (isAnswerSubmitted && isCorrect)) FontWeight.Bold else FontWeight.Medium,
                                    color = itemText
                                )
                            }
                        }
                    }

                    // Explanation Box
                    if (isAnswerSubmitted) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Indigo500.copy(alpha = 0.1f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "💡 VAISHU Insight: ${currentQ.explanation}",
                                fontSize = 12.sp,
                                color = textPrimary,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Bottom Action Button
                    Button(
                        onClick = {
                            if (!isAnswerSubmitted) {
                                if (selectedOptionIndex != null) {
                                    isAnswerSubmitted = true
                                    if (selectedOptionIndex == currentQ.correctIndex) {
                                        score += 1
                                    }
                                }
                            } else {
                                if (currentIndex < questions.size - 1) {
                                    currentIndex += 1
                                    selectedOptionIndex = null
                                    isAnswerSubmitted = false
                                } else {
                                    isQuizCompleted = true
                                }
                            }
                        },
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo500,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = if (!isAnswerSubmitted) "Check Answer" else if (currentIndex < questions.size - 1) "Next Question" else "View Score",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Completed Screen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Indigo500),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Quiz Finished! 🎉",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "You scored $score / ${questions.size} correct",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Lavender500
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "+150 XP added to your Daily Orbit",
                        fontSize = 12.sp,
                        color = textSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onCompleteQuiz(score, questions.size)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo500,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Done", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
