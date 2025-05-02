package com.rit.tunely.ui.screens.leaderboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.data.model.User

@Composable
fun LeaderboardScreen(nav: NavController, vm: LeaderboardViewModel = hiltViewModel()) {
    val s = vm.state.collectAsState().value
    Column(Modifier.fillMaxSize()) {
        Text(
            "Leaderboard", modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
        if (s.isLoading) Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Loading…") }
        else LazyColumn {
            itemsIndexed(s.users) { i: Int, u: User ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${i + 1}."); Spacer(Modifier.width(8.dp)); Text(u.username); Spacer(Modifier.weight(1f)); Text(u.points.toString())
                }
            }
        }
    }
}
