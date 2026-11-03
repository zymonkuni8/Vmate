package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightBackground

/**
 * Renders the signature organic S-wave dividing curve inspired by the reference image.
 * Gives the screen smooth dual-tone depth (claymorphic sculpted feel).
 */
@Composable
fun OrganicCurveBackdrop(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean
) {
    val topGradient = if (isDarkMode) {
        listOf(
            Color(0xFF0F1226),
            Color(0xFF181C38),
            Color(0xFF1E234A)
        )
    } else {
        listOf(
            Color(0xFFE0E7FF),
            Color(0xFFEDE9FE),
            Color(0xFFF3F4FB)
        )
    }

    val bottomColor = if (isDarkMode) DarkBackground else LightBackground

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Fill base bottom color
            drawRect(color = bottomColor)

            // 2. Draw curved upper organic wave
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(width, 0f)
                // Draw down on right side
                lineTo(width, height * 0.32f)

                // Smooth S-curve to left side
                cubicTo(
                    x1 = width * 0.75f,
                    y1 = height * 0.36f,
                    x2 = width * 0.35f,
                    y2 = height * 0.22f,
                    x3 = 0f,
                    y3 = height * 0.30f
                )
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = topGradient,
                    startY = 0f,
                    endY = height * 0.35f
                )
            )

            // Subtle glowing accent aura
            val auraCenterY = height * 0.15f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        (if (isDarkMode) Indigo500 else Lavender500).copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = androidx.compose.ui.geometry.Offset(width * 0.8f, auraCenterY),
                    radius = width * 0.6f
                ),
                radius = width * 0.6f,
                center = androidx.compose.ui.geometry.Offset(width * 0.8f, auraCenterY)
            )
        }
    }
}
