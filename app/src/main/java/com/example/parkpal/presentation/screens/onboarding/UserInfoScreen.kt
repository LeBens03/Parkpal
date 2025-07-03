package com.example.parkpal.presentation.screens.onboarding

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parkpal.presentation.viewmodel.UserViewModel
import com.example.parkpal.domain.model.User
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.parkpal.presentation.viewmodel.AuthViewModel

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
                title = { Text("Complete your profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                Text("Continue")
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
                label = { Text("Name") },
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                label = { Text("Email") },
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
                    text = "Please enter a valid email",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = phoneNumber,
                label = { Text("Phone Number") },
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = city,
                label = { Text("City") },
                onValueChange = { city = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = birthDate,
                label = { Text("Birthdate (YYYY-MM-DD)") },
                onValueChange = { birthDate = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = gender,
                label = { Text("Gender") },
                onValueChange = { gender = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = address,
                label = { Text("Address") },
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = country,
                label = { Text("Country") },
                onValueChange = { country = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = zipCode,
                label = { Text("Zip Code") },
                onValueChange = { zipCode = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
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
                label = { Text("Confirm Password") },
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (passwordError) {
                Text(
                    text = "Passwords have to match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showSuccessToast) {
                Toast.makeText(context, "Signup successful!", Toast.LENGTH_LONG).show()
                showSuccessToast = false
            }
        }
    }
}