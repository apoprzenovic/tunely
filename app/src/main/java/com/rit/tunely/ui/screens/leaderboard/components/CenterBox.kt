package com.rit.tunely.ui.screens.leaderboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CenterBox(content: @Composable BoxScope.() -> Unit) =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = content)