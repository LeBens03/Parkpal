package com.example.parkpal.presentation.screens.main

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val currentUser by userViewModel.currentUser.observeAsState()
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var showPasswordChangeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            currentUser?.let {
                Text("Change Password", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    placeholder = { Text("Enter old password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    placeholder = { Text("Enter new password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        passwordError?.let { passwordError = null }
                    },
                    label = { Text("Confirm Password") },
                    placeholder = { Text("Re-enter new password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val isPasswordValid = (newPassword == confirmPassword) && (newPassword.isNotBlank() && oldPassword.isNotBlank())

                        if (!isPasswordValid) {
                            passwordError = when {
                                newPassword != confirmPassword -> "Passwords do not match"
                                oldPassword.isEmpty() -> "Old password cannot be empty"
                                newPassword.isEmpty() -> "New password cannot be empty"
                                else -> null
                            }
                            return@Button
                        }

                        authViewModel.updatePassword(
                            oldPassword,
                            newPassword,
                            onSuccess = {
                                passwordError = null
                                showPasswordChangeDialog = true
                            },
                            onError = { returnedErrorMessage ->
                                passwordError = returnedErrorMessage
                            }
                        )

                        val updatedUser = currentUser?.copy(password = newPassword)
                        updatedUser?.let { userViewModel.updateUser(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Password")
                }

                if (showPasswordChangeDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showPasswordChangeDialog = false
                            onBack()
                        },
                        title = { Text("Password Change") },
                        text = { Text("Your password has been changed successfully.") },
                        confirmButton = {
                            TextButton(onClick = { showPasswordChangeDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }

    }
}