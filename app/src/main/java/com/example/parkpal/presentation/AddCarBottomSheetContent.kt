package com.example.parkpal.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.parkpal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarBottomSheetContent(
    onSave: (brand: String, model: String, year: Int, licensePlate: String) -> Unit,
    onDismiss: () -> Unit
) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.add_new_car), style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text(stringResource(id = R.string.car_brand)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text(stringResource(id = R.string.car_model)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text(stringResource(id = R.string.year)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = licensePlate,
            onValueChange = { licensePlate = it },
            label = { Text(stringResource(id = R.string.license_plate)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }

            Button(onClick = {
                onSave(brand, model, year.toIntOrNull() ?: 0, licensePlate)
            }) {
                Text(stringResource(id = R.string.save))
            }
        }
    }
}