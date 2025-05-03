package com.rit.tunely.ui.screens.game.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(onStart: () -> Unit) = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
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
}
