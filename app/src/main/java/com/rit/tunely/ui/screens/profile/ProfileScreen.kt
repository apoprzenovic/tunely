package com.rit.tunely.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.rit.tunely.components.AnimatedColorButton
import com.rit.tunely.navigation.TunelyDestinations
import com.rit.tunely.ui.screens.game.PastelGreen
import com.rit.tunely.ui.screens.game.PastelRed

@Composable
fun ProfileScreen(
    nav: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    LaunchedEffect(Unit) { vm.loadProfile(uid) }

    val state = vm.state.collectAsState().value

    var bioDraft by remember { mutableStateOf(state.user.bio) }
    LaunchedEffect(state.user.bio) { bioDraft = state.user.bio }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Username + points
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.user.username.ifBlank { "Username not defined" },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Points: ${state.user.points}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Text(
            text = state.user.bio.ifBlank { "Bio not defined" },
            style = MaterialTheme.typography.bodyMedium
        )

        // Bio editor
        OutlinedTextField(
            value = bioDraft,
            onValueChange = { bioDraft = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            label = { Text("Bio") }
        )

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedColorButton(
                text = "Save",
                baseColor = PastelGreen,
                textColor = Color.Black,
                modifier = Modifier.weight(1f)
            ) {
                vm.updateBio(bioDraft)
                vm.saveProfile()
            }

            Spacer(Modifier.width(16.dp))

            AnimatedColorButton(
                text = "Log out",
                baseColor = PastelRed,
                textColor = Color.Black,
                modifier = Modifier.weight(1f)
            ) {
                FirebaseAuth.getInstance().signOut()
                nav.navigate(TunelyDestinations.LOGIN.name) {
                    popUpTo(TunelyDestinations.PROFILE.name) { inclusive = true }
                }
            }
        }
    }
}

