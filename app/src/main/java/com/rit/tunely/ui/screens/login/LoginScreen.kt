package com.rit.tunely.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rit.tunely.components.AnimatedColorButton
import com.rit.tunely.navigation.TunelyDestinations
import com.rit.tunely.ui.screens.game.Purple200

@Composable
fun LoginScreen(
    nav: NavController,
    vm: LoginViewModel = hiltViewModel()
) {
    val s = vm.state.collectAsState().value
    val focus = LocalFocusManager.current

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "TUNELY", fontSize = 36.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 388.dp)
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = s.email,
                onValueChange = vm::onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = s.password,
                onValueChange = vm::onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            AnimatedColorButton(
                text = "Sign In",
                enabled = !s.isLoading,
                onClick = {
                    focus.clearFocus()
                    vm.login {
                        nav.navigate(TunelyDestinations.MAIN.name) {
                            popUpTo(TunelyDestinations.LOGIN.name) { inclusive = true }
                        }
                    }
                },
                baseColor = Color.Transparent,
                textColor = Color.Black,
                borderStroke = BorderStroke(1.dp, Purple200),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            TextButton({ nav.navigate(TunelyDestinations.SIGN_UP.name) }) { Text("Sign Up") }

            s.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}

