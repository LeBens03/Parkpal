package com.example.parkpal.presentation.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.parkpal.domain.model.User
import com.example.parkpal.presentation.viewmodel.UserViewModel
import com.example.parkpal.R
import com.example.parkpal.ui.theme.SpaceExtraSmall
import com.example.parkpal.ui.theme.SpaceMedium
import com.example.parkpal.ui.theme.SpaceSmall
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val currentUser by userViewModel.currentUser.observeAsState()
    var isEditable by remember { mutableStateOf(false) }
    val modifiedUser = remember {
        mutableStateOf(
            currentUser ?: User(
                name = "",
                phoneNumber = null,
                email = "",
                gender = null,
                birthDate = "",
                address = null,
                city = "",
                country = null,
                zipCode = null,
                password = null,
            )
        )
    }

    var showSuccessToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val genderOptions = listOf("Male", "Female", "Other")
    var genderExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        currentUser?.let { modifiedUser.value = it.copy() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.personal_info)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { isEditable = !isEditable }) {
                        Text(text = if (isEditable) stringResource(id = R.string.cancel) else stringResource(id = R.string.edit))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SpaceMedium, vertical = SpaceMedium)
                    .padding(bottom = 72.dp) // space for the button
            ) {
                // Your editable fields (same as before)
                EditableField(
                    label = stringResource(id = R.string.name),
                    value = modifiedUser.value.name,
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(name = it) }
                )
                EditableField(
                    label = stringResource(id = R.string.phone_number),
                    value = modifiedUser.value.phoneNumber ?: "",
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(phoneNumber = it) }
                )
                EditableField(
                    label = stringResource(id = R.string.email),
                    value = modifiedUser.value.email,
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(email = it) }
                )
                // Gender dropdown, birthdate, etc. same as before
                Text(
                    text = stringResource(id = R.string.gender),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = SpaceExtraSmall)
                )
                if (isEditable) {
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = modifiedUser.value.gender ?: "",
                            onValueChange = {},
                            readOnly = true,
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
                                        modifiedUser.value = modifiedUser.value.copy(gender = option)
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    DisplayField(value = modifiedUser.value.gender ?: "-")
                }
                Spacer(modifier = Modifier.height(SpaceMedium))

                EditableField(
                    label = stringResource(id = R.string.date_of_birth),
                    value = modifiedUser.value.birthDate,
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(birthDate = it) }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))

                EditableField(
                    label = stringResource(id = R.string.address),
                    value = modifiedUser.value.address ?: "",
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(address = it) }
                )
                EditableField(
                    label = stringResource(id = R.string.city),
                    value = modifiedUser.value.city,
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(city = it) }
                )
                EditableField(
                    label = stringResource(id = R.string.country),
                    value = modifiedUser.value.country ?: "",
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(country = it) }
                )
                EditableField(
                    label = stringResource(id = R.string.zip_code),
                    value = modifiedUser.value.zipCode ?: "",
                    isEditable = isEditable,
                    onValueChange = { modifiedUser.value = modifiedUser.value.copy(zipCode = it) }
                )
            }

            if (isEditable) {
                Button(
                    onClick = {
                        userViewModel.updateUser(modifiedUser.value)
                        isEditable = false
                        showSuccessToast = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpaceMedium)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(stringResource(id = R.string.save))
                }
            }

            if (showSuccessToast) {
                Toast.makeText(context, stringResource(id = R.string.info_updated), Toast.LENGTH_LONG).show()
                showSuccessToast = false
            }
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = SpaceExtraSmall)
        )
        if (isEditable) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            DisplayField(value = value.ifBlank { "-" })
        }
        Spacer(modifier = Modifier.height(SpaceMedium))
    }
}

@Composable
fun DisplayField(value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpaceSmall)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(SpaceMedium)
        )
    }
}
