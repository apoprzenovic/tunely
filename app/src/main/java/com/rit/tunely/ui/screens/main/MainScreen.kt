package com.rit.tunely.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rit.tunely.navigation.TunelyDestinations
import com.rit.tunely.ui.screens.game.GameScreen
import com.rit.tunely.ui.screens.leaderboard.LeaderboardScreen
import com.rit.tunely.ui.screens.profile.ProfileScreen

@Composable
fun MainScreen(parentNavController: NavController) {
    val tabNavController = rememberNavController()

    val items = listOf(
        NavigationItem(
            route = TunelyDestinations.GAME.name,
            title = "Game",
            icon = Icons.Filled.MusicNote
        ),
        NavigationItem(
            route = TunelyDestinations.LEADERBOARD.name,
            title = "Leaderboard",
            icon = Icons.Rounded.Leaderboard
        ),
        NavigationItem(
            route = TunelyDestinations.PROFILE.name,
            title = "Profile",
            icon = Icons.Rounded.AccountCircle
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            tabNavController.navigate(item.route) {
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = TunelyDestinations.GAME.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TunelyDestinations.GAME.name) {
                GameScreen(tabNavController)
            }
            composable(TunelyDestinations.LEADERBOARD.name) {
                LeaderboardScreen(tabNavController)
            }
            composable(TunelyDestinations.PROFILE.name) {
                ProfileScreen(parentNavController)
            }
        }
    }
}