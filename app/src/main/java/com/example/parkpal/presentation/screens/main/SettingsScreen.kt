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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.parkpal.R
import com.example.parkpal.presentation.viewmodel.AuthViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

import com.example.parkpal.ui.theme.*
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

    val passwordsDoNotMatch = stringResource(R.string.passwords_do_not_match)
    val oldPasswordEmpty = stringResource(R.string.old_password_empty)
    val newPasswordEmpty = stringResource(R.string.new_password_empty)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
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
                .padding(horizontal = SpaceLarge, vertical = SpaceMedium)
                .verticalScroll(rememberScrollState())
        ) {
            currentUser?.let {
                Text(
                    stringResource(id = R.string.change_password),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(bottom = SpaceLarge)
                        .align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text(stringResource(id = R.string.old_password)) },
                    placeholder = { Text(stringResource(id = R.string.enter_old_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpaceMedium),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = passwordError == oldPasswordEmpty
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(stringResource(id = R.string.new_password)) },
                    placeholder = { Text(stringResource(id = R.string.enter_new_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpaceMedium),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = passwordError == newPasswordEmpty
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (passwordError != null) passwordError = null
                    },
                    label = { Text(stringResource(id = R.string.confirm_password)) },
                    placeholder = { Text(stringResource(id = R.string.reenter_new_password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpaceSmall),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = passwordError == passwordsDoNotMatch
                )

                if (passwordError != null) {
                    Text(
                        text = passwordError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = SpaceMedium)
                    )
                } else {
                    Spacer(modifier = Modifier.height(SpaceMedium))
                }

                Button(
                    onClick = {
                        val isPasswordValid =
                            (newPassword == confirmPassword) && newPassword.isNotBlank() && oldPassword.isNotBlank()

                        if (!isPasswordValid) {
                            passwordError = when {
                                newPassword != confirmPassword -> passwordsDoNotMatch
                                oldPassword.isEmpty() -> oldPasswordEmpty
                                newPassword.isEmpty() -> newPasswordEmpty
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

                        currentUser?.copy(password = newPassword)?.let { userViewModel.updateUser(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.reset_password))
                }

                if (showPasswordChangeDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showPasswordChangeDialog = false
                            onBack()
                        },
                        title = { Text(stringResource(id = R.string.password_change_title)) },
                        text = { Text(stringResource(id = R.string.password_change_success)) },
                        confirmButton = {
                            TextButton(onClick = { showPasswordChangeDialog = false }) {
                                Text(stringResource(id = R.string.ok_button))
                            }
                        }
                    )
                }
            }
        }
    }
}