package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Claymorphic modifier that adds soft 3D matte depth:
 * - Upper-left highlight
 * - Bottom-right soft drop shadow
 * - Clean smooth rounded corners
 */
fun Modifier.clayEffect(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color,
    highlightColor: Color = Color.White.copy(alpha = 0.7f),
    shadowColor: Color = Color(0xFFC4CCE5).copy(alpha = 0.6f),
    elevation: Dp = 6.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.White.copy(alpha = 0.5f)
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = shadowColor,
        spotColor = shadowColor
    )
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                highlightColor.copy(alpha = 0.35f),
                backgroundColor,
                backgroundColor
            ),
            start = Offset(0f, 0f),
            end = Offset(400f, 400f)
        ),
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                borderColor,
                borderColor.copy(alpha = 0.1f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 300f)
        ),
        shape = shape
    )
    .clip(shape)

/**
 * Glassmorphic translucent frosted surface:
 * - Soft semi-transparent backdrop
 * - Gentle glow / border shine
 */
fun Modifier.glassEffect(
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.65f),
    borderColor: Color = Color.White.copy(alpha = 0.45f),
    borderWidth: Dp = 1.dp,
    shadowElevation: Dp = 4.dp
): Modifier = this
    .shadow(
        elevation = shadowElevation,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.04f),
        spotColor = Color.Black.copy(alpha = 0.08f)
    )
    .background(
        color = backgroundColor,
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                borderColor,
                borderColor.copy(alpha = 0.15f)
            ),
            start = Offset(0f, 0f),
            end = Offset(250f, 400f)
        ),
        shape = shape
    )
    .clip(shape)

/**
 * Soft pill clay button modifier
 */
fun Modifier.clayPill(
    backgroundColor: Color,
    isDark: Boolean = false
): Modifier {
    val highlight = if (isDark) Color(0x33FFFFFF) else Color(0x99FFFFFF)
    val shadow = if (isDark) Color(0xFF0A0C16) else Color(0xFFCCD4EE)
    return this.clayEffect(
        shape = RoundedCornerShape(50),
        backgroundColor = backgroundColor,
        highlightColor = highlight,
        shadowColor = shadow,
        elevation = 5.dp
    )
}
