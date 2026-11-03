package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
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

@Composable
fun StudentOnboardingScreen(
    isDarkMode: Boolean,
    initialProfile: StudentProfile,
    onComplete: (fullName: String, email: String, rollNumber: String, schoolName: String, className: String, section: String) -> Unit
) {
    var fullName by remember { mutableStateOf(initialProfile.fullName) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var rollNumber by remember { mutableStateOf(initialProfile.rollNumber) }
    var schoolName by remember { mutableStateOf(initialProfile.schoolName) }
    var className by remember { mutableStateOf(initialProfile.className) }
    var section by remember { mutableStateOf(initialProfile.section) }

    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("student_onboarding_screen"),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Mascot & Title
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(GradientPurpleBlue)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Student Setup",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Student Profile Setup",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Enter your classroom details to tailor your study plan",
                fontSize = 13.sp,
                color = textSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Onboarding Form in Glass Panel
        item {
            GlassPanel(
                modifier = Modifier.fillMaxWidth(),
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Full Name
                    OnboardingInputField(
                        label = "Full Name",
                        value = fullName,
                        onValueChange = { fullName = it },
                        icon = Icons.Default.Person,
                        placeholder = "e.g. Amrit Kumar",
                        isDarkMode = isDarkMode,
                        testTag = "onboarding_name_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Email
                    OnboardingInputField(
                        label = "Email Address",
                        value = email,
                        onValueChange = { email = it },
                        icon = Icons.Default.Email,
                        placeholder = "e.g. amritk72316@gmail.com",
                        isDarkMode = isDarkMode,
                        testTag = "onboarding_email_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // School Name
                    OnboardingInputField(
                        label = "School / Academy Name",
                        value = schoolName,
                        onValueChange = { schoolName = it },
                        icon = Icons.Default.School,
                        placeholder = "e.g. St. Jude's International Academy",
                        isDarkMode = isDarkMode,
                        testTag = "onboarding_school_input"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Roll Number, Class, Section Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OnboardingInputField(
                                label = "Roll No.",
                                value = rollNumber,
                                onValueChange = { rollNumber = it },
                                icon = Icons.Default.Numbers,
                                placeholder = "24",
                                isDarkMode = isDarkMode,
                                testTag = "onboarding_roll_input"
                            )
                        }

                        Box(modifier = Modifier.weight(1.2f)) {
                            OnboardingInputField(
                                label = "Class",
                                value = className,
                                onValueChange = { className = it },
                                icon = Icons.Default.School,
                                placeholder = "Class 11",
                                isDarkMode = isDarkMode,
                                testTag = "onboarding_class_input"
                            )
                        }

                        Box(modifier = Modifier.weight(0.8f)) {
                            OnboardingInputField(
                                label = "Section",
                                value = section,
                                onValueChange = { section = it },
                                icon = Icons.Default.Badge,
                                placeholder = "A",
                                isDarkMode = isDarkMode,
                                testTag = "onboarding_section_input"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // Action Button
        item {
            Button(
                onClick = {
                    onComplete(fullName, email, rollNumber, schoolName, className, section)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_complete_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Indigo500,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Complete & Enter My Orbit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
private fun OnboardingInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String,
    isDarkMode: Boolean,
    testTag: String
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF13172E) else Color(0xFFFFFFFF)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textSecondary,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(2.dp, RoundedCornerShape(18.dp))
                .background(fieldBg, RoundedCornerShape(18.dp))
                .border(
                    width = 1.dp,
                    color = if (isDarkMode) Color(0x33818CF8) else Color(0x606366F1),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Lavender500,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = placeholder,
                            fontSize = 13.sp,
                            color = textSecondary.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(testTag),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
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
