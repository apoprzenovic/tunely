package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.rit.tunely.ui.components.AnimatedColorButton
import com.rit.tunely.ui.theme.PastelGreen
import com.rit.tunely.ui.theme.PastelRed

@Composable
fun HintDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hint:") },
        text = { Text("Guess the title based on the 30 second preview of the song playing.") },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedColorButton(
                    text = "OK",
                    onClick = onDismiss,
                    baseColor = Color.White,
                    textColor = Color.Black
                )
            }
        }
    )
}

@Composable
fun GameEndDialog(
    gameWon: Boolean,
    pointsEarned: Int?,
    onContinue: () -> Unit
) {
    val backgroundColor = if (gameWon) PastelGreen else PastelRed
    val message = buildAnnotatedString {
        if (gameWon) {
            append("Congratulations you've correctly guessed the title!")
            append("\n")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Earned Points: $pointsEarned")
            }
        } else {
            append("You didn't manage to guess correctly, better luck next time!")
        }
    }

    AlertDialog(
        containerColor = backgroundColor,
        onDismissRequest = { /* Prevent dismissing by clicking outside */ },
        confirmButton = { /* content for custom layout */ },
        dismissButton = { /* content for custom layout */ },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = message, color = Color.Black)
                Spacer(Modifier.height(16.dp))
                AnimatedColorButton(
                    text = "Continue",
                    baseColor = Color.White,
                    textColor = Color.Black,
                    onClick = onContinue,
                )
            }
        }
    )
}