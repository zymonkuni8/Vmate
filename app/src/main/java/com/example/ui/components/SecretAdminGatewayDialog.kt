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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect

private enum class GatewayStep {
    SECRET_KEY_PROMPT,
    ACCESSING_ANIMATION,
    CHOOSE_PORTAL,
    TEACHER_LOGIN,
    PRINCIPAL_LOGIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretAdminGatewayDialog(
    isDarkMode: Boolean,
    onVerifySecretKey: suspend (String) -> Boolean,
    onTeacherLogin: (email: String, accessCode: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onPrincipalLogin: (email: String, passcode: String, onResult: (Boolean, String?) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(GatewayStep.SECRET_KEY_PROMPT) }
    var enteredSecretKey by remember { mutableStateOf("") }
    var secretKeyError by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }

    // Teacher Login Form State
    var teacherEmail by remember { mutableStateOf("s.jenkins@stjude.edu") }
    var teacherAccessCode by remember { mutableStateOf("TCH-7421") }
    var teacherError by remember { mutableStateOf<String?>(null) }
    var isTeacherLoggingIn by remember { mutableStateOf(false) }

    // Principal Login Form State
    var principalEmail by remember { mutableStateOf("principal@stjude.edu") }
    var principalPasscode by remember { mutableStateOf("ADMIN-STERLING-2026") }
    var principalError by remember { mutableStateOf<String?>(null) }
    var isPrincipalLoggingIn by remember { mutableStateOf(false) }

    val bgColor = if (isDarkMode) Color(0xFF14182E) else Color(0xFFFFFFFF)
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val fieldBg = if (isDarkMode) Color(0xFF1B203E) else Color(0xFFF8FAFC)

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("secret_admin_gateway_dialog")
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
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(GradientPurpleBlue)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "School Staff Gateway",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Authorized Personnel Only",
                                fontSize = 11.sp,
                                color = Lavender500
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = step,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "gateway_step_transition"
                ) { currentStep ->
                    when (currentStep) {
                        GatewayStep.SECRET_KEY_PROMPT -> {
                            Column {
                                Text(
                                    text = "Secret Administration Access",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Enter the school-configured administration credential to unlock authorized staff entry.",
                                    fontSize = 12.sp,
                                    color = textSecondary,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                TextField(
                                    value = enteredSecretKey,
                                    onValueChange = {
                                        enteredSecretKey = it
                                        secretKeyError = null
                                    },
                                    placeholder = {
                                        Text("e.g. VAISHU-GATEWAY-2026", fontSize = 13.sp, color = textSecondary.copy(alpha = 0.5f))
                                    },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(fieldBg)
                                        .border(
                                            1.dp,
                                            if (secretKeyError != null) Color(0xFFEF4444) else if (isDarkMode) Color(0x33818CF8) else Color(0x506366F1),
                                            RoundedCornerShape(16.dp)
                                        ),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = fieldBg,
                                        unfocusedContainerColor = fieldBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                if (secretKeyError != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = secretKeyError ?: "",
                                        fontSize = 11.sp,
                                        color = Color(0xFFEF4444),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        if (enteredSecretKey.isBlank()) {
                                            secretKeyError = "Please enter the secret access credential"
                                            return@Button
                                        }
                                        isVerifying = true
                                        // Run verification through coroutine / callback
                                        scope.launch {
                                            val isValid = onVerifySecretKey(enteredSecretKey)
                                            isVerifying = false
                                            if (isValid) {
                                                step = GatewayStep.ACCESSING_ANIMATION
                                                delay(1100)
                                                step = GatewayStep.CHOOSE_PORTAL
                                            } else {
                                                secretKeyError = "Invalid administration credential. Access denied."
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("verify_secret_admin_button"),
                                    enabled = !isVerifying,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Indigo500,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    if (isVerifying) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Unlock Administration", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Mandatory specification message: "🔐 Accessing Secure Administration"
                        GatewayStep.ACCESSING_ANIMATION -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Indigo500.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = Indigo500,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "🔐 Accessing Secure Administration",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Credential verified. Preparing authorized portals...",
                                    fontSize = 12.sp,
                                    color = Lavender500
                                )
                            }
                        }

                        // Portal Selection (Teacher Login or Principal Login)
                        GatewayStep.CHOOSE_PORTAL -> {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF10B981).copy(alpha = 0.12f))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "🔐 Accessing Secure Administration",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Select Authorization Portal",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Option 1: Teacher Login
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clayEffect(
                                            shape = RoundedCornerShape(20.dp),
                                            backgroundColor = if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF),
                                            borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x606366F1),
                                            elevation = 4.dp
                                        )
                                        .clickable { step = GatewayStep.TEACHER_LOGIN }
                                        .padding(16.dp)
                                        .testTag("choose_teacher_portal_button")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CuteClayIcon(
                                            icon = Icons.Default.School,
                                            contentDescription = "Teacher Login",
                                            gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                                            size = 44.dp,
                                            iconSize = 22.dp,
                                            isDarkMode = isDarkMode
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("Teacher Login", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                            Text("Access assigned class rosters and student submissions", fontSize = 11.sp, color = textSecondary)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Option 2: Principal / Master Admin Login
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clayEffect(
                                            shape = RoundedCornerShape(20.dp),
                                            backgroundColor = if (isDarkMode) Color(0xFF181C38) else Color(0xFFFFFFFF),
                                            borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x606366F1),
                                            elevation = 4.dp
                                        )
                                        .clickable { step = GatewayStep.PRINCIPAL_LOGIN }
                                        .padding(16.dp)
                                        .testTag("choose_principal_portal_button")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CuteClayIcon(
                                            icon = Icons.Default.AdminPanelSettings,
                                            contentDescription = "Principal Login",
                                            gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFF97316)),
                                            size = 44.dp,
                                            iconSize = 22.dp,
                                            isDarkMode = isDarkMode
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("Principal / Master Admin", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                            Text("School-wide management, teachers, rules, & security", fontSize = 11.sp, color = textSecondary)
                                        }
                                    }
                                }
                            }
                        }

                        // Portal Form: Teacher Login
                        GatewayStep.TEACHER_LOGIN -> {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Teacher Authentication", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text(
                                        text = "Back",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Indigo500,
                                        modifier = Modifier.clickable { step = GatewayStep.CHOOSE_PORTAL }
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Requires an authorized school teacher account and Master Admin access code.",
                                    fontSize = 11.sp,
                                    color = textSecondary
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text("Teacher School Email", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = teacherEmail,
                                    onValueChange = {
                                        teacherEmail = it
                                        teacherError = null
                                    },
                                    placeholder = { Text("e.g. s.jenkins@stjude.edu", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(fieldBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = fieldBg,
                                        unfocusedContainerColor = fieldBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Teacher Access Code", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = teacherAccessCode,
                                    onValueChange = {
                                        teacherAccessCode = it
                                        teacherError = null
                                    },
                                    placeholder = { Text("e.g. TCH-7421", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(fieldBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = fieldBg,
                                        unfocusedContainerColor = fieldBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                if (teacherError != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(teacherError ?: "", fontSize = 11.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        isTeacherLoggingIn = true
                                        onTeacherLogin(teacherEmail, teacherAccessCode) { success, errMsg ->
                                            isTeacherLoggingIn = false
                                            if (!success) {
                                                teacherError = errMsg ?: "Verification failed."
                                            } else {
                                                onDismiss()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("teacher_submit_login_button"),
                                    enabled = !isTeacherLoggingIn,
                                    colors = ButtonDefaults.buttonColors(containerColor = Indigo500, contentColor = Color.White),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    if (isTeacherLoggingIn) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                    } else {
                                        Text("Sign In as Teacher", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Portal Form: Principal / Master Admin Login
                        GatewayStep.PRINCIPAL_LOGIN -> {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Principal / Master Admin", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text(
                                        text = "Back",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Indigo500,
                                        modifier = Modifier.clickable { step = GatewayStep.CHOOSE_PORTAL }
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Requires verified principal administrator credentials.",
                                    fontSize = 11.sp,
                                    color = textSecondary
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text("Principal School Email", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = principalEmail,
                                    onValueChange = {
                                        principalEmail = it
                                        principalError = null
                                    },
                                    placeholder = { Text("e.g. principal@stjude.edu", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(fieldBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = fieldBg,
                                        unfocusedContainerColor = fieldBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Master Admin Passcode", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                TextField(
                                    value = principalPasscode,
                                    onValueChange = {
                                        principalPasscode = it
                                        principalError = null
                                    },
                                    placeholder = { Text("Enter Master Passcode", fontSize = 12.sp) },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(fieldBg),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = fieldBg,
                                        unfocusedContainerColor = fieldBg,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = textPrimary,
                                        unfocusedTextColor = textPrimary
                                    ),
                                    singleLine = true
                                )

                                if (principalError != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(principalError ?: "", fontSize = 11.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        isPrincipalLoggingIn = true
                                        onPrincipalLogin(principalEmail, principalPasscode) { success, errMsg ->
                                            isPrincipalLoggingIn = false
                                            if (!success) {
                                                principalError = errMsg ?: "Verification failed."
                                            } else {
                                                onDismiss()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("principal_submit_login_button"),
                                    enabled = !isPrincipalLoggingIn,
                                    colors = ButtonDefaults.buttonColors(containerColor = Indigo500, contentColor = Color.White),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    if (isPrincipalLoggingIn) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                    } else {
                                        Text("Sign In as Master Admin", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
