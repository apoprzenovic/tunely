package com.rit.tunely.ui.screens.leaderboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rit.tunely.data.model.User
import com.rit.tunely.ui.theme.Bronze
import com.rit.tunely.ui.theme.Gold
import com.rit.tunely.ui.theme.Silver

@Composable
fun LeaderboardRow(rank: Int, user: User) {

    val medal: Pair<Color, @Composable () -> Unit> = when (rank) {
        1 -> Gold to { Icon(Icons.Filled.EmojiEvents, null, tint = Color(0xFFB8860B)) }
        2 -> Silver to { Text("2.", fontWeight = FontWeight.Bold) }
        3 -> Bronze to { Text("3.", fontWeight = FontWeight.Bold) }
        else -> Color.Transparent to { Text("$rank.") }
    }

    val bg = medal.first
    val prefix = medal.second

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            prefix()
            Spacer(Modifier.width(12.dp))
            Text(user.username, style = MaterialTheme.typography.bodyLarge)
        }
        Text("${user.points} pts", fontWeight = FontWeight.SemiBold)
    }
}
