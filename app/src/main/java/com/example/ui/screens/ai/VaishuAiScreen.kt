package com.example.ui.screens.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.ChatMessage
import com.example.data.model.LearningMode
import com.example.data.model.UserRole
import com.example.ui.components.ClaySurfaceCard
import com.example.ui.components.GlassPanel
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
import com.example.ui.theme.glassEffect

@Composable
fun VaishuAiScreen(
    isDarkMode: Boolean,
    activeRole: UserRole,
    chatMessages: List<ChatMessage>,
    chatInput: String,
    isAiThinking: Boolean,
    selectedLearningMode: LearningMode = LearningMode.LEARN,
    onSelectLearningMode: (LearningMode) -> Unit = {},
    onChatInputChange: (String) -> Unit,
    onSendMessage: (String?) -> Unit,
    onClearChat: () -> Unit
) {
    val listState = rememberLazyListState()

    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LaunchedEffect(chatMessages.size, isAiThinking) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    // Role and Mode-specific educational suggestions
    val quickPills = when (activeRole) {
        UserRole.STUDENT -> when (selectedLearningMode) {
            LearningMode.LEARN -> listOf(
                "Explain photosynthesis.",
                "Teach me this chapter.",
                "Help me revise.",
                "Quantum physics simplified"
            )
            LearningMode.TUTORIAL -> listOf(
                "Give me a simple tutorial.",
                "Step-by-step calculus differentiation",
                "How does DNA replication work?",
                "Newton's laws of motion tutorial"
            )
            LearningMode.PRACTICE -> listOf(
                "Generate practice questions.",
                "3 practice problems on algebra",
                "Quiz me.",
                "Wave optics practice"
            )
            LearningMode.TEST -> listOf(
                "Generate a structured test.",
                "5-question chemistry revision test",
                "Cell biology test",
                "Kinematics mock test"
            )
            LearningMode.EXPLAIN -> listOf(
                "Explain photosynthesis.",
                "Explain this worksheet question",
                "Break down Chapter 4 notes",
                "Explain the lab formula"
            )
            LearningMode.HINT -> listOf(
                "I don't understand this math problem.",
                "Give me a hint for Question 2",
                "Clue for balancing this reaction",
                "Hint without giving away the answer"
            )
        }
        UserRole.TEACHER -> listOf(
            "📋 45-Min Lesson Plan",
            "🎯 Formative Assessment Questions",
            "🔬 Physics Lab Rubric",
            "💡 Differentiated Learning Tips"
        )
        UserRole.OTHER, UserRole.PRINCIPAL -> listOf(
            "📊 School Attendance Trends",
            "📈 Curriculum Mastery Audit",
            "🛡️ AI Academic Integrity Check",
            "🏛️ Faculty Workload Summary"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("vaishu_ai_screen")
    ) {
        // AI Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            ClaySurfaceCard(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(GradientPurpleBlue)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "VAISHU AI",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "VAISHU AI Assistant",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "${activeRole.displayName} Learning Co-Pilot",
                                fontSize = 11.sp,
                                color = Lavender500
                            )
                        }
                    }

                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier.testTag("clear_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Clear Chat",
                            tint = textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Dedicated 6 Learning Modes Strip (LEARN, TUTORIAL, PRACTICE, TEST, EXPLAIN, HINT)
        if (activeRole == UserRole.STUDENT) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(LearningMode.values()) { mode ->
                        val isSelected = selectedLearningMode == mode
                        val (icon, label) = when (mode) {
                            LearningMode.LEARN -> Pair(Icons.Default.School, "LEARN")
                            LearningMode.TUTORIAL -> Pair(Icons.AutoMirrored.Filled.MenuBook, "TUTORIAL")
                            LearningMode.PRACTICE -> Pair(Icons.Default.Science, "PRACTICE")
                            LearningMode.TEST -> Pair(Icons.Default.Psychology, "TEST")
                            LearningMode.EXPLAIN -> Pair(Icons.Default.Description, "EXPLAIN")
                            LearningMode.HINT -> Pair(Icons.Default.Lightbulb, "HINT")
                        }

                        val pillBg = if (isSelected) {
                            if (isDarkMode) Color(0xFF3730A3) else Color(0xFF4F46E5)
                        } else {
                            if (isDarkMode) Color(0xFF161A34) else Color(0xFFFFFFFF)
                        }

                        Box(
                            modifier = Modifier
                                .clayEffect(
                                    shape = RoundedCornerShape(50),
                                    backgroundColor = pillBg,
                                    borderColor = if (isSelected) Color(0xFFA5B4FC) else if (isDarkMode) Color(0x33818CF8) else Color(0x406366F1),
                                    elevation = if (isSelected) 5.dp else 2.dp
                                )
                                .clickable { onSelectLearningMode(mode) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("learning_mode_${mode.name}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color.White else Indigo500,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else textPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Active mode guidance banner
                Text(
                    text = "Mode: ${selectedLearningMode.description}",
                    fontSize = 11.sp,
                    color = Lavender500,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(chatMessages) { message ->
                if (message.isUser) {
                    UserChatBubble(
                        message = message,
                        isDarkMode = isDarkMode
                    )
                } else {
                    AiChatBubble(
                        message = message,
                        isDarkMode = isDarkMode,
                        onSuggestionClick = { prompt -> onSendMessage(prompt) }
                    )
                }
            }

            if (isAiThinking) {
                item {
                    AiThinkingBubble(isDarkMode = isDarkMode)
                }
            }
        }

        // Quick Suggestion Pills
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPills) { pillText ->
                Box(
                    modifier = Modifier
                        .clayEffect(
                            shape = RoundedCornerShape(50),
                            backgroundColor = if (isDarkMode) Color(0xFF1B203B) else Color(0xFFFFFFFF),
                            borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x606366F1),
                            elevation = 3.dp
                        )
                        .clickable { onSendMessage(pillText) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("quick_suggestion_$pillText")
                ) {
                    Text(
                        text = pillText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) DarkTextPrimary else LightTextPrimary
                    )
                }
            }
        }

        // Bottom Input Area (Soft clay container)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 100.dp)
        ) {
            GlassPanel(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = chatInput,
                        onValueChange = onChatInputChange,
                        placeholder = {
                            Text(
                                text = "Ask VAISHU anything for school...",
                                color = textSecondary.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(4.dp, CircleShape)
                            .background(Brush.linearGradient(GradientPurpleBlue), CircleShape)
                            .clickable { onSendMessage(null) }
                            .testTag("send_chat_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserChatBubble(
    message: ChatMessage,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .shadow(4.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(
                    brush = Brush.linearGradient(GradientPurpleBlue),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun AiChatBubble(
    message: ChatMessage,
    isDarkMode: Boolean,
    onSuggestionClick: (String) -> Unit
) {
    val bubbleBg = if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clayEffect(
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 22.dp, bottomStart = 22.dp, bottomEnd = 22.dp),
                    backgroundColor = bubbleBg,
                    borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF),
                    elevation = 4.dp
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Lavender500),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "VAISHU",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo500
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = textPrimary,
                    lineHeight = 21.sp
                )

                if (message.suggestions.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    val suggestions = message.suggestions.split(",")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        suggestions.forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Indigo500.copy(alpha = 0.1f))
                                    .border(1.dp, Indigo500.copy(alpha = 0.3f), RoundedCornerShape(50))
                                    .clickable { onSuggestionClick(suggestion.trim()) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "✨ ${suggestion.trim()}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
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

@Composable
private fun AiThinkingBubble(isDarkMode: Boolean) {
    val bubbleBg = if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clayEffect(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = bubbleBg,
                    elevation = 4.dp
                )
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Indigo500,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "VAISHU is reasoning...",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Indigo500
                )
            }
        }
    }
}
