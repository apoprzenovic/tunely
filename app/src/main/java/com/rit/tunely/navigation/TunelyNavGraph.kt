package com.rit.tunely.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rit.tunely.ui.screens.login.LoginScreen
import com.rit.tunely.ui.screens.main.MainScreen
import com.rit.tunely.ui.screens.signup.SignUpScreen

@Composable
fun TunelyNavGraph() {
    val nav = rememberNavController()
    NavHost(nav, TunelyDestinations.LOGIN.name) {
        composable(TunelyDestinations.LOGIN.name) { LoginScreen(nav) }
        composable(TunelyDestinations.SIGN_UP.name) { SignUpScreen(nav) }
        composable(TunelyDestinations.MAIN.name) { MainScreen(nav) }
    }
}