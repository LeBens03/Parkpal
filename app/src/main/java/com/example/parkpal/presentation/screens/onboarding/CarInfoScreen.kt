package com.example.parkpal.presentation.screens.onboarding

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.parkpal.domain.model.Car
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

@Composable
fun CarInfoScreen(
    userViewModel: UserViewModel,
    carViewModel: CarViewModel,
    onCarSaved: () -> Unit,
) {
    var brand by remember { mutableStateOf(TextFieldValue("")) }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var licensePlate by remember { mutableStateOf(TextFieldValue("")) }

    var showSuccessToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentUser by userViewModel.currentUser.observeAsState()
    Log.d("CarInfoScreen", "Current user: $currentUser")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Car Information", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = licensePlate,
            onValueChange = { licensePlate = it },
            label = { Text("License Plate") },
            modifier = Modifier.fillMaxWidth()
        )

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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }

        if (showSuccessToast) {
            Toast.makeText(context, "Car added successfully!", Toast.LENGTH_LONG).show()
            showSuccessToast = false
        }
    }
}
