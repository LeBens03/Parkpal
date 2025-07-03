package com.example.parkpal.presentation.screens.main

import android.util.Log
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

    Log.d("PersonalInfoScreen", "User: $currentUser")

    LaunchedEffect(currentUser) {
        currentUser?.let { modifiedUser.value = it.copy() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.personal_info)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
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
        },
        floatingActionButton = {
            if (isEditable) {
                FloatingActionButton(
                    onClick = {
                        userViewModel.updateUser(modifiedUser.value)
                        isEditable = false
                        showSuccessToast = true
                    }
                ) {
                    Text(stringResource(id = R.string.save))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            EditableField(stringResource(id = R.string.name), modifiedUser.value.name, isEditable) { modifiedUser.value = modifiedUser.value.copy(name = it) }
            EditableField(stringResource(id = R.string.phone_number), modifiedUser.value.phoneNumber ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(phoneNumber = it) }
            EditableField(stringResource(id = R.string.email), modifiedUser.value.email, isEditable) { modifiedUser.value = modifiedUser.value.copy(email = it) }
            EditableField(stringResource(id = R.string.gender), modifiedUser.value.gender ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(gender = it) }
            EditableField(stringResource(id = R.string.date_of_birth), modifiedUser.value.birthDate, isEditable) { modifiedUser.value = modifiedUser.value.copy(birthDate = it) }
            EditableField(stringResource(id = R.string.address), modifiedUser.value.address ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(address = it) }
            EditableField(stringResource(id = R.string.city), modifiedUser.value.city, isEditable) { modifiedUser.value = modifiedUser.value.copy(city = it) }
            EditableField(stringResource(id = R.string.country), modifiedUser.value.country ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(country = it) }
            EditableField(stringResource(id = R.string.zip_code), modifiedUser.value.zipCode ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(zipCode = it) }

            Spacer(modifier = Modifier.height(16.dp))

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
        Text(text = label)
        if (isEditable) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}