package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardClay
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Lavender500
import com.example.ui.theme.LightCardClay
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.clayEffect
import com.example.ui.theme.glassEffect

@Composable
fun ClaySurfaceCard(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    shape: Shape = RoundedCornerShape(26.dp),
    elevation: Dp = 6.dp,
    onClick: (() -> Unit)? = null,
    testTag: String = "clay_card",
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f, label = "press_scale")

    val bgColor = if (isDarkMode) DarkCardClay else LightCardClay
    val shadowColor = if (isDarkMode) Color(0xFF060710) else Color(0xFFCCD4EE)
    val highlightColor = if (isDarkMode) Color(0x22818CF8) else Color(0xFFFFFFFF)
    val borderColor = if (isDarkMode) Color(0x33818CF8) else Color(0x80FFFFFF)

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .testTag(testTag)
            .scale(scale)
            .clayEffect(
                shape = shape,
                backgroundColor = bgColor,
                highlightColor = highlightColor,
                shadowColor = shadowColor,
                elevation = if (isPressed) 2.dp else elevation,
                borderColor = borderColor
            )
            .then(clickableModifier)
    ) {
        content()
    }
}

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    shape: Shape = RoundedCornerShape(28.dp),
    testTag: String = "glass_panel",
    content: @Composable () -> Unit
) {
    val bgColor = if (isDarkMode) Color(0xD0171B33) else Color(0xD8FFFFFF)
    val borderColor = if (isDarkMode) Color(0x33A5B4FC) else Color(0x99FFFFFF)

    Box(
        modifier = modifier
            .testTag(testTag)
            .glassEffect(
                shape = shape,
                backgroundColor = bgColor,
                borderColor = borderColor,
                shadowElevation = 8.dp
            )
    ) {
        content()
    }
}

/**
 * Cute 3D styled icon wrapper with glowing gradient background and soft clay depth
 */
@Composable
fun CuteClayIcon(
    icon: ImageVector,
    contentDescription: String,
    gradientColors: List<Color>,
    size: Dp = 54.dp,
    iconSize: Dp = 26.dp,
    isDarkMode: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = gradientColors.first().copy(alpha = 0.3f),
                spotColor = gradientColors.last().copy(alpha = 0.4f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Vertical rounded pill indicator, directly styled after the reference image's date/meter pills.
 */
@Composable
fun VerticalMetricCapsule(
    dayNumber: String,
    monthText: String,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val activeGradient = listOf(Indigo500, Lavender500)
    val inactiveBg = if (isDarkMode) Color(0xFF1E2342) else Color(0xFFE2E7F5)

    val textColor = if (isSelected) Color.White else if (isDarkMode) DarkTextSecondary else LightTextSecondary

    Box(
        modifier = Modifier
            .width(52.dp)
            .height(105.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 2.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = if (isSelected) Indigo500.copy(alpha = 0.4f) else Color.Transparent,
                spotColor = if (isSelected) Lavender500.copy(alpha = 0.4f) else Color.Transparent
            )
            .background(
                brush = if (isSelected) {
                    Brush.verticalGradient(activeGradient)
                } else {
                    Brush.verticalGradient(listOf(inactiveBg, inactiveBg))
                },
                shape = RoundedCornerShape(26.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color.White.copy(alpha = 0.6f) else Color.Transparent,
                shape = RoundedCornerShape(26.dp)
            )
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayNumber,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else if (isDarkMode) DarkTextPrimary else LightTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = monthText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}
