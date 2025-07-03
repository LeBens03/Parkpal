package com.example.parkpal.presentation.screens.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.parkpal.presentation.viewmodel.CarViewModel
import androidx.compose.runtime.getValue
import com.example.parkpal.domain.model.Car
import androidx.compose.material.icons.Icons
import com.example.parkpal.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.parkpal.presentation.AddCarBottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyVehicleScreen(
    userId: Long,
    carViewModel: CarViewModel,
    onBack: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    var showAddToast by remember { mutableStateOf(false) }
    var showDeleteToast by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        carViewModel.getCarByUserId(userId)
    }

    val currentUserCars by carViewModel.currentUserCars.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Vehicles") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            showBottomSheet = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Vehicle"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (currentUserCars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No vehicles found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentUserCars) { car ->
                    VehicleCard(vehicle = car, onDelete = {
                        carViewModel.deleteCar(car)
                        showDeleteToast = true
                    })
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            AddCarBottomSheetContent(
                onSave = { brand, model, year, licensePlate ->
                    val newCar = Car(
                        userId = userId,
                        brand = brand,
                        model = model,
                        year = year,
                        licensePlate = licensePlate
                    )
                    carViewModel.insertCar(newCar)
                    showBottomSheet = false
                    showAddToast = true
                },
                onDismiss = { showBottomSheet = false }
            )
        }
    }

    if (showAddToast) {
        Toast.makeText(context, "Car added successfully", Toast.LENGTH_SHORT).show()
        showAddToast = false
    }

    // Show Toast for Delete Car
    if (showDeleteToast) {
        Toast.makeText(context, "Car deleted successfully", Toast.LENGTH_SHORT).show()
        showDeleteToast = false
    }
}

@Composable
fun VehicleCard(
    vehicle: Car,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Car icon on far left
            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_car_24),
                contentDescription = "Car Icon",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = vehicle.brand, style = MaterialTheme.typography.titleMedium)
                Text(text = "Model: ${vehicle.model}", style = MaterialTheme.typography.bodyMedium)
            }

            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}