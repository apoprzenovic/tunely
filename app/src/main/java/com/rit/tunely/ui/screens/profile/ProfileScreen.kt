package com.rit.tunely.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.rit.tunely.navigation.TunelyDestinations

@Composable
fun ProfileScreen(nav: NavController, vm: ProfileViewModel = hiltViewModel()) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    LaunchedEffect(Unit) { vm.loadProfile(uid) }
    val s = vm.state.collectAsState().value
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(s.user.username.ifBlank { "User" })
        Spacer(Modifier.height(16.dp))
        Text("Points: ${s.user.points}")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(s.user.bio, vm::updateBio, label = { Text("Bio") })
        Spacer(Modifier.height(16.dp))
        Button(vm::saveProfile) { Text("Save") }
        Spacer(Modifier.height(8.dp))
        Button({
            FirebaseAuth.getInstance().signOut()
            nav.navigate(TunelyDestinations.LOGIN.name) {
                popUpTo(TunelyDestinations.PROFILE.name) { inclusive = true }
            }
        }) { Text("Log out") }
    }
}
