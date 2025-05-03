package com.rit.tunely.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedColorButton(
    text: String,
    baseColor: Color,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderStroke: BorderStroke = BorderStroke(1.dp, Color.Transparent),
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val animatedColor by animateColorAsState(
        if (pressed) baseColor.copy(alpha = 0.85f)
        else baseColor,
        label = "buttonColorAnim"
    )

    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        interactionSource = interaction,
        colors = ButtonDefaults.buttonColors(containerColor = animatedColor),
        enabled = enabled,
        border = borderStroke,
    ) { Text(text = text, color = textColor) }
}