package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rit.tunely.ui.screens.game.PastelGreen
import com.rit.tunely.ui.screens.game.PastelRed

@Composable
fun HintDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hint") },
        text = { Text("Guess the title!") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun GameEndDialog(
    gameWon: Boolean,
    onContinue: () -> Unit
) {
    val backgroundColor = if (gameWon) PastelGreen else PastelRed
    val message = if (gameWon) {
        "Congratulations you've correctly guessed the title!"
    } else {
        "You didn't manage to guess correctly, better luck next time!"
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
                Button(onClick = onContinue) {
                    Text("Continue")
                }
            }
        }
    )
}