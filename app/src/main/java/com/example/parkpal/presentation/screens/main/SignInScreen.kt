package com.example.parkpal.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import com.example.parkpal.presentation.viewmodel.AuthState
import com.example.parkpal.R
import com.example.parkpal.ui.theme.*

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onSignIn()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.lets_you_in),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = SpaceLarge)
        )

        Spacer(modifier = Modifier.height(SpaceLarge))

        // Google Sign-In Button with logo
        Button(
            onClick = { authViewModel.signInWithGoogle(context) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // Match TextField height
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = ElevationMedium)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = stringResource(R.string.google_logo_content_description),
                modifier = Modifier.size(20.dp), // smaller, fits nicely
                tint = Color.Unspecified // Ensures original colors are kept
            )
            Spacer(modifier = Modifier.width(SpaceSmall))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpaceSmall)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = stringResource(R.string.or_divider),
                modifier = Modifier.padding(horizontal = SpaceSmall),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError) emailError = false
            },
            label = { Text(stringResource(R.string.email)) },
            isError = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            singleLine = true
        )
        if (emailError) {
            Text(
                text = stringResource(R.string.invalid_email_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = SpaceExtraSmall)
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError) passwordError = false
            },
            label = { Text(stringResource(R.string.password)) },
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            singleLine = true
        )
        if (passwordError) {
            Text(
                text = stringResource(R.string.password_empty_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = SpaceExtraSmall)
            )
        }

        Spacer(modifier = Modifier.height(SpaceLarge))

        // Login button
        Button(
            onClick = {
                val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                val isPasswordValid = password.isNotBlank()

                emailError = !isEmailValid
                passwordError = !isPasswordValid

                if (isEmailValid && isPasswordValid) {
                    authViewModel.signIn(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                stringResource(R.string.login),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        // Sign up prompt
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.no_account_question),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(SpaceSmall))
            Text(
                text = stringResource(R.string.sign_up),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onSignUp() },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}