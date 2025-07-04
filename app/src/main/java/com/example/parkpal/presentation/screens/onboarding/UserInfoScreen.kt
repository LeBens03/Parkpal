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
import androidx.compose.ui.Alignment
import com.example.parkpal.ui.theme.SpaceMedium

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
    val genderOptions = listOf("Male", "Female", "Other")
    var genderExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.complete_profile)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceMedium),
                contentAlignment = Alignment.Center
            ) {
                Button(
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
                            showSuccessToast = true
                            onSaveUser()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(stringResource(R.string.continue_button))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // — Personal Info —
            Text(
                text = stringResource(R.string.personal_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(stringResource(R.string.phone_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text(stringResource(R.string.birthdate_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text(stringResource(R.string.city)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Gender dropdown
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.gender)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(R.string.address)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text(stringResource(R.string.country)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = zipCode,
                onValueChange = { zipCode = it },
                label = { Text(stringResource(R.string.zip_code)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // — Account Info —
            Text(
                text = stringResource(R.string.account_info),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError) emailError = false
                },
                isError = emailError,
                label = { Text(stringResource(R.string.email)) },
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
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

            Spacer(modifier = Modifier.height(100.dp)) // To avoid FAB overlap
        }
    }
}
