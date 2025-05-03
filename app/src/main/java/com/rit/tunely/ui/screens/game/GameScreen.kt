package com.rit.tunely.ui.screens.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    nav: NavController,
    vm: GameViewModel = hiltViewModel()
) {
    val state = vm.state.collectAsState().value

    LaunchedEffect(Unit) {
        vm.init()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Guess the Title") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tries left: ${state.attemptsLeft}")
            Spacer(Modifier.height(16.dp))
            Text(state.maskedTitle, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = state.currentGuess,
                onValueChange = vm::onGuessChange,
                label = { Text("Your guess") }
            )
            Spacer(Modifier.height(8.dp))
            Button(
                enabled = state.currentGuess.isNotBlank(),
                onClick = vm::submitGuess
            ) { Text("Submit") }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}

