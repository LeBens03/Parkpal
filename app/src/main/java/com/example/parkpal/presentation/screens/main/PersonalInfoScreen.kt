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
import androidx.compose.ui.unit.dp
import com.example.parkpal.domain.model.User
import com.example.parkpal.presentation.viewmodel.UserViewModel

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

    // Update modifiedUser when currentUser changes
    LaunchedEffect(currentUser) {
        currentUser?.let { modifiedUser.value = it.copy() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Personal Info") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Default back icon
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { isEditable = !isEditable }) {
                        Text(text = if (isEditable) "Cancel" else "Edit")
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
                    Text("Save")
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

            // User Information Fields
            EditableField("Name", modifiedUser.value.name, isEditable) { modifiedUser.value = modifiedUser.value.copy(name = it) }
            EditableField("Phone Number", modifiedUser.value.phoneNumber ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(phoneNumber = it) }
            EditableField("Email", modifiedUser.value.email, isEditable) { modifiedUser.value = modifiedUser.value.copy(email = it) }
            EditableField("Gender", modifiedUser.value.gender ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(gender = it) }
            EditableField("Date of Birth", modifiedUser.value.birthDate, isEditable) { modifiedUser.value = modifiedUser.value.copy(birthDate = it) }
            EditableField("Address", modifiedUser.value.address ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(address = it) }
            EditableField("City", modifiedUser.value.city, isEditable) { modifiedUser.value = modifiedUser.value.copy(city = it) }
            EditableField("Country", modifiedUser.value.country ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(country = it) }
            EditableField("Zip Code", modifiedUser.value.zipCode ?: "", isEditable) { modifiedUser.value = modifiedUser.value.copy(zipCode = it) }

            Spacer(modifier = Modifier.height(16.dp))

            if (showSuccessToast) {
                Toast.makeText(context, "Your information has been updated!", Toast.LENGTH_LONG).show()
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