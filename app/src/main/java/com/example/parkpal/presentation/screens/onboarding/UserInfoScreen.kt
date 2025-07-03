package com.example.parkpal.presentation.screens.onboarding

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.parkpal.R
import com.example.parkpal.domain.model.User
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    onSaveUser: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var showSuccessToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentUser by userViewModel.currentUser.observeAsState()
    Log.d("UserInfoScreen", "Current user: $currentUser")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.complete_profile)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val isPasswordValid = password.isNotBlank() && password == confirmPassword

                    emailError = !isEmailValid
                    passwordError = !isPasswordValid

                    if (isEmailValid && isPasswordValid) {
                        val user = User(
                            name = name,
                            email = email,
                            password = password,
                            phoneNumber = phoneNumber.takeIf { it.isNotBlank() },
                            gender = gender.takeIf { it.isNotBlank() },
                            address = address.takeIf { it.isNotBlank() },
                            city = city,
                            country = country.takeIf { it.isNotBlank() },
                            zipCode = zipCode.takeIf { it.isNotBlank() },
                            birthDate = birthDate,
                        )
                        userViewModel.insertUser(user)
                        authViewModel.signUp(email, password)
                        onSaveUser()
                        showSuccessToast = true
                    }
                }
            ) {
                Text(stringResource(R.string.continue_button))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                label = { Text(stringResource(R.string.name)) },
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                label = { Text(stringResource(R.string.email)) },
                onValueChange = {
                    email = it
                    if (emailError) emailError = false
                },
                isError = emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text(
                    text = stringResource(R.string.please_enter_valid_email),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = phoneNumber,
                label = { Text(stringResource(R.string.phone_number)) },
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = city,
                label = { Text(stringResource(R.string.city)) },
                onValueChange = { city = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = birthDate,
                label = { Text(stringResource(R.string.birthdate_label)) },
                onValueChange = { birthDate = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = gender,
                label = { Text(stringResource(R.string.gender)) },
                onValueChange = { gender = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = address,
                label = { Text(stringResource(R.string.address)) },
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = country,
                label = { Text(stringResource(R.string.country)) },
                onValueChange = { country = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = zipCode,
                label = { Text(stringResource(R.string.zip_code)) },
                onValueChange = { zipCode = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (passwordError) passwordError = false
                },
                label = { Text(stringResource(R.string.confirm_password)) },
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (passwordError) {
                Text(
                    text = stringResource(R.string.passwords_have_to_match),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showSuccessToast) {
                Toast.makeText(context, stringResource(R.string.signup_successful), Toast.LENGTH_LONG).show()
                showSuccessToast = false
            }
        }
    }
}
