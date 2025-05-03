package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(onStart: () -> Unit) = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onStart,
            modifier = Modifier.size(120.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.PlayCircle,
                contentDescription = "Play Game",
                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "Tap to start",
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
