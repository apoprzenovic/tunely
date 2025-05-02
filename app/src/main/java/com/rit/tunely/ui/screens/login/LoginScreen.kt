package com.rit.tunely.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.navigation.TunelyDestinations

@Composable
fun LoginScreen(nav: NavController, vm: LoginViewModel = hiltViewModel()) {
    val s = vm.state.collectAsState().value
    val focus = LocalFocusManager.current

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            Text("Sign In")
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(s.email, vm::onEmailChange, label = { Text("Email") }, singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                s.password, vm::onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                enabled = !s.isLoading,
                onClick = {
                    focus.clearFocus()
                    vm.login {
                        nav.navigate(TunelyDestinations.MAIN.name) {
                            popUpTo(TunelyDestinations.LOGIN.name) { inclusive = true }
                        }
                    }
                }
            ) { Text("Sign In") }
            Spacer(Modifier.height(8.dp))
            TextButton({ nav.navigate(TunelyDestinations.SIGN_UP.name) }) { Text("Sign Up") }
            s.error?.let { Text(it) }
        }
    }
}
