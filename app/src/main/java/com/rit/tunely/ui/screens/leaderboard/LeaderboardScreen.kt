package com.rit.tunely.ui.screens.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.ui.components.ScreenTopBar
import com.rit.tunely.ui.screens.leaderboard.components.CenterBox
import com.rit.tunely.ui.screens.leaderboard.components.LeaderboardRow

@Composable
fun LeaderboardScreen(
    nav: NavController,
    vm: LeaderboardViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTopBar("Leaderboard")

        when {
            state.isLoading -> CenterBox { CircularProgressIndicator() }
            state.error != null -> CenterBox {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }

            state.users.isEmpty() -> CenterBox { Text("Leaderboard is empty.") }

            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.users.take(10)) { index, user ->
                    LeaderboardRow(rank = index + 1, user = user)
                }
            }
        }
    }
}