package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rit.tunely.ui.screens.game.BackgroundGray

@Composable
fun OnScreenKeyboard(
    modifier: Modifier = Modifier,
    onCharClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    val row1 = "QWERTYUIOP"
    val row2 = "ASDFGHJKL"
    val row3 = "ZXCVBNM"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        KeyboardRow(row1, onCharClick)
        KeyboardRow(row2, onCharClick)
        KeyboardRowWithSpecial(row3, onCharClick, onEnterClick, onBackspaceClick)
    }
}

@Composable
private fun KeyboardRow(
    keys: String,
    onCharClick: (Char) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        keys.forEach { char ->
            KeyButton(
                modifier = Modifier.weight(1f),
                onClick = { onCharClick(char) }
            ) {
                Text(char.toString(), fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun KeyboardRowWithSpecial(
    letters: String,
    onCharClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        KeyButton(
            modifier = Modifier.weight(1.5f),
            onClick = onEnterClick
        ) { Text("ENTER", fontSize = 12.sp) }

        letters.forEach { char ->
            KeyButton(
                modifier = Modifier.weight(1f),
                onClick = { onCharClick(char) }
            ) { Text(char.toString(), fontSize = 16.sp) }
        }

        KeyButton(
            modifier = Modifier.weight(1.5f),
            onClick = onBackspaceClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace"
            )
        }
    }
}

@Composable
private fun KeyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp),
        contentPadding = PaddingValues(0.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = BackgroundGray)
    ) {
        content()
    }
}
