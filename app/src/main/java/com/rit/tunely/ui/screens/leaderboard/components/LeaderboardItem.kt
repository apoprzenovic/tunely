package com.rit.tunely.ui.screens.leaderboard.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rit.tunely.data.model.User

@Composable
fun LeaderboardItem(rank: Int, user: User) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank.",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(30.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = user.username,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = user.points.toString(),
            fontWeight = FontWeight.SemiBold
        )
    }
}