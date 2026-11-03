package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.model.QuizQuestion
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

private enum class RapidQuizStep {
    CONFIG,
    PLAYING,
    RESULT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RapidQuestionsDialog(
    isDarkMode: Boolean,
    onGenerateQuestions: (count: Int, subject: String, topic: String, chapter: String) -> List<QuizQuestion>,
    onCompleteRapidQuiz: (count: Int, score: Int, total: Int, subject: String, topic: String) -> Unit,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(RapidQuizStep.CONFIG) }
    var selectedCount by remember { mutableStateOf(5) } // 5, 10, 15
    var selectedSubject by remember { mutableStateOf("Advanced Mathematics") }
    var topicInput by remember { mutableStateOf("Calculus & Derivatives") }
    var chapterInput by remember { mutableStateOf("Chapter 3") }

    // Gameplay state
    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var hasAnsweredCurrent by remember { mutableStateOf(false) }

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("rapid_questions_dialog")
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
                .padding(22.dp)
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
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "⚡ RAPID QUESTIONS",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textPrimary
                            )
                            Text(
                                text = "Dynamic AI Speed Sprint",
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

                AnimatedContent(
                    targetState = step,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "rapid_quiz_step"
                ) { currentStep ->
                    when (currentStep) {
                        RapidQuizStep.CONFIG -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Question Count Options: Quick 5, Quick 10, Quick 15
                                Text(
                                    text = "Choose Sprint Length",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textSecondary
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val options = listOf(
                                        Pair(5, "Quick 5\n(5 Qs • 5m)"),
                                        Pair(10, "Quick 10\n(10 Qs • 10m)"),
                                        Pair(15, "Quick 15\n(15 Qs • 15m)")
                                    )

                                    options.forEach { (count, label) ->
                                        val isSelected = selectedCount == count
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(if (isSelected) Indigo500 else cardBg)
                                                .border(1.dp, if (isSelected) Indigo500 else Color(0x306366F1), RoundedCornerShape(16.dp))
                                                .clickable { selectedCount = count }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else textPrimary,
                                                lineHeight = 15.sp,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Subject Selector
                                Text("Select Subject", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val subjects = listOf("Advanced Mathematics", "Quantum Physics", "Cellular Biology", "AI & Computer Science")
                                    items(subjects) { subj ->
                                        val isSelected = selectedSubject == subj
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) Indigo500 else cardBg)
                                                .clickable { selectedSubject = subj }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = subj,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) Color.White else textSecondary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Topic & Chapter Inputs
                                Text("Topic / Learning Focus", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = topicInput,
                                    onValueChange = { topicInput = it },
                                    placeholder = { Text("e.g. Photosynthesis or Matrices", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(cardBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = cardBg,
                                        unfocusedContainerColor = cardBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Chapter (Optional)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = chapterInput,
                                    onValueChange = { chapterInput = it },
                                    placeholder = { Text("e.g. Chapter 4", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(cardBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = cardBg,
                                        unfocusedContainerColor = cardBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        val generated = onGenerateQuestions(selectedCount, selectedSubject, topicInput, chapterInput)
                                        questions = generated
                                        currentIndex = 0
                                        selectedOptionIndex = null
                                        score = 0
                                        hasAnsweredCurrent = false
                                        step = RapidQuizStep.PLAYING
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("start_rapid_sprint_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Indigo500,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Launch Rapid $selectedCount Questions ⚡", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        RapidQuizStep.PLAYING -> {
                            if (questions.isNotEmpty() && currentIndex < questions.size) {
                                val currentQ = questions[currentIndex]

                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // Progress bar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Question ${currentIndex + 1} of ${questions.size}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Lavender500
                                        )
                                        Text(
                                            text = "Score: $score",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF10B981)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    LinearProgressIndicator(
                                        progress = { (currentIndex + 1).toFloat() / questions.size.toFloat() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(50)),
                                        color = Indigo500,
                                        trackColor = if (isDarkMode) Color(0xFF1E2344) else Color(0xFFE2E8F0)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Question Card
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(cardBg)
                                            .padding(14.dp)
                                    ) {
                                        Text(
                                            text = currentQ.question,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary,
                                            lineHeight = 19.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // 4 Options
                                    currentQ.options.forEachIndexed { optIndex, optionText ->
                                        val isSelected = selectedOptionIndex == optIndex
                                        val isCorrect = optIndex == currentQ.correctIndex

                                        val optBg = when {
                                            !hasAnsweredCurrent -> cardBg
                                            isCorrect -> Color(0xFF10B981).copy(alpha = 0.2f)
                                            isSelected -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                            else -> cardBg
                                        }

                                        val optBorder = when {
                                            !hasAnsweredCurrent -> if (isDarkMode) Color(0x33818CF8) else Color(0x306366F1)
                                            isCorrect -> Color(0xFF10B981)
                                            isSelected -> Color(0xFFEF4444)
                                            else -> Color.Transparent
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(optBg)
                                                .border(1.dp, optBorder, RoundedCornerShape(14.dp))
                                                .clickable(enabled = !hasAnsweredCurrent) {
                                                    selectedOptionIndex = optIndex
                                                    hasAnsweredCurrent = true
                                                    if (optIndex == currentQ.correctIndex) {
                                                        score++
                                                    }
                                                }
                                                .padding(12.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                val letter = listOf("A", "B", "C", "D").getOrElse(optIndex) { "$optIndex" }
                                                Box(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected || (hasAnsweredCurrent && isCorrect)) Indigo500 else Color(0x206366F1)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = letter,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected || (hasAnsweredCurrent && isCorrect)) Color.White else textSecondary
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = optionText,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = textPrimary,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }

                                    if (hasAnsweredCurrent) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (selectedOptionIndex == currentQ.correctIndex) Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFFEF4444).copy(alpha = 0.1f))
                                                .padding(10.dp)
                                        ) {
                                            Text(
                                                text = "💡 ${currentQ.explanation}",
                                                fontSize = 11.sp,
                                                color = textPrimary,
                                                lineHeight = 15.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Button(
                                            onClick = {
                                                if (currentIndex + 1 < questions.size) {
                                                    currentIndex++
                                                    selectedOptionIndex = null
                                                    hasAnsweredCurrent = false
                                                } else {
                                                    onCompleteRapidQuiz(selectedCount, score, questions.size, selectedSubject, topicInput)
                                                    step = RapidQuizStep.RESULT
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(44.dp)
                                                .testTag("rapid_next_button"),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Indigo500,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Text(
                                                text = if (currentIndex + 1 < questions.size) "Next Question →" else "Finish & View Score 🎉",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        RapidQuizStep.RESULT -> {
                            val earnedXp = (score * 20).coerceIn(40, 150)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Rapid Sprint Completed! 🎉",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "You scored $score out of ${questions.size} in $selectedSubject",
                                    fontSize = 12.sp,
                                    color = textSecondary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color(0xFFF59E0B).copy(alpha = 0.15f))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "+$earnedXp Verified XP Earned!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD97706)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color(0xFFF97316).copy(alpha = 0.15f))
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Daily Challenge Completed & Streak Maintained! 🔥",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFEA580C)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Indigo500,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text("Done", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
