package com.rit.tunely.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
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
    val navController = rememberNavController()

    val items = listOf(
        NavigationItem(
            route = TunelyDestinations.GAME.name,
            title = "Game",
            icon = Icons.Default.PlayArrow
        ),
        NavigationItem(
            route = TunelyDestinations.LEADERBOARD.name,
            title = "Leaderboard",
            icon = Icons.Default.Menu
        ),
        NavigationItem(
            route = TunelyDestinations.PROFILE.name,
            title = "Profile",
            icon = Icons.Default.Person
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
            navController = navController,
            startDestination = TunelyDestinations.GAME.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TunelyDestinations.GAME.name) {
                GameScreen(navController)
            }
            composable(TunelyDestinations.LEADERBOARD.name) {
                LeaderboardScreen(navController)
            }
            composable(TunelyDestinations.PROFILE.name) {
                ProfileScreen(navController)
            }
        }
    }
}