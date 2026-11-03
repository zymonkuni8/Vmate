package com.example.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.GradientPurpleBlue
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender400
import com.example.ui.theme.Lavender500
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashLoadingScreen(
    isDarkMode: Boolean,
    onFinish: () -> Unit
) {
    // 7-step sequence animatables
    val bgAlpha = remember { Animatable(0f) }
    val particlesAlpha = remember { Animatable(0f) }
    val vStrokeProgress = remember { Animatable(0f) }
    val faceFormProgress = remember { Animatable(0f) }
    val faceGlow = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val pulseScale = remember { Animatable(0.95f) }

    LaunchedEffect(Unit) {
        // 1. Background softly appears
        bgAlpha.animateTo(1f, tween(400, easing = LinearEasing))

        // 2. Small glowing particles/lines appear
        particlesAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))

        // 3 & 4. V-shaped visual identity forms
        launch {
            vStrokeProgress.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        }

        // 5. Complete VAISHU / V-Mate face mark becomes visible
        delay(250)
        faceFormProgress.animateTo(1f, tween(500, easing = FastOutSlowInEasing))

        // 6. Subtle glow settles around it
        faceGlow.animateTo(1f, tween(400, easing = FastOutSlowInEasing))

        // 7. Underneath display "VAISHU" and "by Zymon"
        textAlpha.animateTo(1f, tween(400, easing = LinearEasing))

        // Gentle breathing pulse
        pulseScale.animateTo(
            targetValue = 1.02f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        // Hold briefly, then transition to Google Login
        delay(600)
        onFinish()
    }

    val bgColor = if (isDarkMode) DarkBackground else Color(0xFFF3F5FC)
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E1B4B)
    val textSub = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable { onFinish() } // tap to fast-forward
            .alpha(bgAlpha.value)
            .testTag("splash_loading_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Glowing orbital backdrop
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (particlesAlpha.value > 0f) {
                // Radial soft glow behind the mascot
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Indigo500.copy(alpha = 0.25f * particlesAlpha.value),
                            Lavender500.copy(alpha = 0.12f * particlesAlpha.value),
                            Color.Transparent
                        ),
                        center = Offset(width / 2f, height * 0.44f),
                        radius = width * 0.55f
                    ),
                    radius = width * 0.55f,
                    center = Offset(width / 2f, height * 0.44f)
                )

                // Ambient glowing circuit dots
                val dotColor = Lavender400.copy(alpha = 0.35f * particlesAlpha.value)
                drawCircle(dotColor, radius = 4f, center = Offset(width * 0.25f, height * 0.35f))
                drawCircle(dotColor, radius = 3f, center = Offset(width * 0.78f, height * 0.38f))
                drawCircle(dotColor, radius = 5f, center = Offset(width * 0.30f, height * 0.55f))
                drawCircle(dotColor, radius = 4f, center = Offset(width * 0.72f, height * 0.52f))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated VAISHU / V-Mate Face Mark
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(pulseScale.value),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Subtle Outer Glow Ring
                    if (faceGlow.value > 0f) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Indigo400.copy(alpha = 0.22f * faceGlow.value),
                                    Color.Transparent
                                ),
                                center = Offset(w / 2f, h / 2f),
                                radius = w * 0.65f
                            ),
                            radius = w * 0.65f,
                            center = Offset(w / 2f, h / 2f)
                        )
                    }

                    // 2. Translucent Clay Head Container
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6366F1).copy(alpha = 0.95f),
                                Color(0xFF8B5CF6).copy(alpha = 0.95f)
                            )
                        ),
                        size = androidx.compose.ui.geometry.Size(w * 0.82f, h * 0.82f),
                        topLeft = Offset(w * 0.09f, h * 0.09f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(34.dp.toPx(), 34.dp.toPx())
                    )

                    // 3. Highlight Specular border
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.75f),
                                Color.White.copy(alpha = 0.15f)
                            )
                        ),
                        size = androidx.compose.ui.geometry.Size(w * 0.82f, h * 0.82f),
                        topLeft = Offset(w * 0.09f, h * 0.09f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(34.dp.toPx(), 34.dp.toPx()),
                        style = Stroke(width = 2.5f)
                    )

                    // 4. V-Shaped Logo Crest forming
                    if (vStrokeProgress.value > 0f) {
                        val vPath = Path().apply {
                            moveTo(w * 0.32f, h * 0.32f)
                            lineTo(w * 0.50f, h * 0.32f + (h * 0.20f * vStrokeProgress.value))
                            lineTo(w * 0.68f, h * 0.32f)
                        }
                        drawPath(
                            path = vPath,
                            color = Color(0xFF38BDF8),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }

                    // 5. Friendly V-Mate Face Elements (Eyes & Smile)
                    if (faceFormProgress.value > 0f) {
                        val eyeAlpha = faceFormProgress.value
                        val eyeColor = Color.White.copy(alpha = eyeAlpha)

                        // Left Eye (glowing vertical pill)
                        drawRoundRect(
                            color = eyeColor,
                            topLeft = Offset(w * 0.36f, h * 0.50f),
                            size = androidx.compose.ui.geometry.Size(8.dp.toPx(), 14.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // Right Eye (glowing vertical pill)
                        drawRoundRect(
                            color = eyeColor,
                            topLeft = Offset(w * 0.64f - 8.dp.toPx(), h * 0.50f),
                            size = androidx.compose.ui.geometry.Size(8.dp.toPx(), 14.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        // Friendly Subtle Smile Curve
                        val smilePath = Path().apply {
                            moveTo(w * 0.44f, h * 0.64f)
                            quadraticTo(
                                w * 0.50f,
                                h * 0.68f,
                                w * 0.56f,
                                h * 0.64f
                            )
                        }
                        drawPath(
                            path = smilePath,
                            color = eyeColor,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Text section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha.value)
            ) {
                Text(
                    text = "VAISHU",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    color = textPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "AI School Learning Orbit",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Lavender500
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mandatory "by Zymon" requirement: clean, small, elegant, not larger than VAISHU identity
                Text(
                    text = "by Zymon",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.5.sp,
                    color = textSub.copy(alpha = 0.75f)
                )
            }
        }
    }
}
