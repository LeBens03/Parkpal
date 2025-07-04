package com.example.parkpal.presentation.screens.onboarding

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.parkpal.R
import com.example.parkpal.domain.model.Car
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel
import com.example.parkpal.ui.theme.SpaceLarge
import com.example.parkpal.ui.theme.SpaceMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarInfoScreen(
    userViewModel: UserViewModel,
    carViewModel: CarViewModel,
    onCarSaved: () -> Unit,
    onBack: () -> Unit
) {
    var brand by remember { mutableStateOf(TextFieldValue("")) }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var licensePlate by remember { mutableStateOf(TextFieldValue("")) }

    var showSuccessToast by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentUser by userViewModel.currentUser.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.car_information)) },
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
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceMedium),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val userId = currentUser?.userId
                        if (userId != null) {
                            val car = Car(
                                userId = userId,
                                brand = brand.text,
                                model = model.text,
                                year = year.text.toIntOrNull() ?: 0,
                                licensePlate = licensePlate.text
                            )
                            carViewModel.insertCar(car)
                            onCarSaved()
                            showSuccessToast = true
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
                .padding(horizontal = SpaceMedium)
                .padding(top = SpaceLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceLarge)
        ) {
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text(stringResource(R.string.brand)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text(stringResource(R.string.model)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text(stringResource(R.string.year)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = licensePlate,
                onValueChange = { licensePlate = it },
                label = { Text(stringResource(R.string.license_plate)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (showSuccessToast) {
                Toast.makeText(context, stringResource(R.string.car_added_successfully), Toast.LENGTH_LONG).show()
                showSuccessToast = false
            }
        }
    }
}