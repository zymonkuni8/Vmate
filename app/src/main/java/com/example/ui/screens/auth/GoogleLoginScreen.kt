package com.example.ui.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ClaySurfaceCard
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
fun GoogleLoginScreen(
    isDarkMode: Boolean,
    onContinueWithGoogle: () -> Unit
) {
    val textPrimary = if (isDarkMode) DarkTextPrimary else LightTextPrimary
    val textSecondary = if (isDarkMode) DarkTextSecondary else LightTextSecondary
    val cardBg = if (isDarkMode) DarkCardClay else LightCardClay

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("google_login_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Floating 3D VAISHU Badge
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clayEffect(
                        shape = RoundedCornerShape(28.dp),
                        backgroundColor = if (isDarkMode) Color(0xFF1E2348) else Color(0xFFFFFFFF),
                        highlightColor = if (isDarkMode) Color(0x33818CF8) else Color.White,
                        shadowColor = if (isDarkMode) Color(0xFF070914) else Color(0xFFC7D2FE),
                        elevation = 8.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(GradientPurpleBlue)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "VAISHU Logo",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Main Glass / Clay Auth Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassEffect(
                        shape = RoundedCornerShape(32.dp),
                        backgroundColor = if (isDarkMode) Color(0xD8171B33) else Color(0xEAFFFFFF),
                        borderColor = if (isDarkMode) Color(0x33A5B4FC) else Color(0x80FFFFFF),
                        shadowElevation = 10.dp
                    )
                    .padding(26.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to VAISHU",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Intelligent AI learning & study companion for students and schools.",
                        fontSize = 13.sp,
                        color = textSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // Primary Action: Continue with Google
                    GoogleSignInButton(
                        isDarkMode = isDarkMode,
                        onClick = onContinueWithGoogle
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = textSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Safe & secure single sign-on with Google",
                            fontSize = 11.sp,
                            color = textSecondary.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "by Zymon",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.5.sp,
                color = textSecondary.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun GoogleSignInButton(
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f, label = "google_btn_scale")

    val bg = if (isDarkMode) Color(0xFF1E223D) else Color(0xFFFFFFFF)
    val shadow = if (isDarkMode) Color(0xFF090A14) else Color(0xFFCCD4EE)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale)
            .clayEffect(
                shape = RoundedCornerShape(50),
                backgroundColor = bg,
                highlightColor = if (isDarkMode) Color(0x33818CF8) else Color.White,
                shadowColor = shadow,
                elevation = 6.dp,
                borderColor = if (isDarkMode) Color(0x40818CF8) else Color(0xFFE2E8F0)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("google_login_button"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Google 'G' Symbol
            GoogleColorGIcon()

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Continue with Google",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) Color.White else Color(0xFF1F2937)
            )
        }
    }
}

@Composable
private fun GoogleColorGIcon() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height

        // Outer circular Google colored arcs
        val blue = Color(0xFF4285F4)
        val green = Color(0xFF34A853)
        val yellow = Color(0xFFFBBC05)
        val red = Color(0xFFEA4335)

        // Draw red arc (top)
        drawArc(
            color = red,
            startAngle = 180f,
            sweepAngle = 100f,
            useCenter = true
        )
        // Draw blue arc (right)
        drawArc(
            color = blue,
            startAngle = 280f,
            sweepAngle = 100f,
            useCenter = true
        )
        // Draw green arc (bottom)
        drawArc(
            color = green,
            startAngle = 20f,
            sweepAngle = 100f,
            useCenter = true
        )
        // Draw yellow arc (left)
        drawArc(
            color = yellow,
            startAngle = 120f,
            sweepAngle = 60f,
            useCenter = true
        )

        // Inner white knockout
        drawCircle(
            color = Color.White,
            radius = w * 0.32f,
            center = Offset(w / 2f, h / 2f)
        )

        // Blue crossbar
        drawRect(
            color = blue,
            topLeft = Offset(w * 0.45f, h * 0.40f),
            size = androidx.compose.ui.geometry.Size(w * 0.55f, h * 0.20f)
        )
    }
}
