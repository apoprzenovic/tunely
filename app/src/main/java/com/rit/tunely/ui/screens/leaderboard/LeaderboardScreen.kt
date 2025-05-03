package com.rit.tunely.ui.screens.leaderboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.ui.components.ScreenTopBar
import com.rit.tunely.ui.screens.leaderboard.components.LeaderboardItem

@Composable
fun LeaderboardScreen(nav: NavController, vm: LeaderboardViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchLeaderboard()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTopBar(title = "Leaderboard")

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }

            state.users.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Leaderboard is empty.")
                }
            }

            else -> {
                LazyColumn {
                    itemsIndexed(state.users) { index, user ->
                        LeaderboardItem(rank = index + 1, user = user)
                    }
                }
            }
        }
    }
}