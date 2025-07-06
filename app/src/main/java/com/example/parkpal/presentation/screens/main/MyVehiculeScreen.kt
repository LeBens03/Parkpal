package com.example.parkpal.presentation.screens.main

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.parkpal.presentation.modals.AddCarBottomSheetContent
import com.example.parkpal.ui.theme.Shapes
import com.example.parkpal.ui.theme.SpaceExtraSmall
import com.example.parkpal.ui.theme.SpaceLarge
import com.example.parkpal.ui.theme.SpaceMedium
import com.example.parkpal.ui.theme.SpaceSmall

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
                title = { Text(stringResource(id = R.string.my_vehicles)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                actions = {
                    Button(
                        onClick = { showBottomSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.add_vehicle)
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
                Text(stringResource(id = R.string.no_vehicles_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(SpaceMedium),
                contentPadding = PaddingValues(SpaceSmall),
                verticalArrangement = Arrangement.spacedBy(SpaceSmall)
            ) {
                items(currentUserCars, key = { it.licensePlate }) { car ->

                    // Create dismiss state per item
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                carViewModel.deleteCar(car)
                                showDeleteToast = true
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = SpaceExtraSmall)
                                    .clip(Shapes.medium)
                                    .background(Color.Red),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.delete),
                                    tint = Color.White,
                                    modifier = Modifier.padding(end = SpaceLarge)
                                )
                            }
                        },
                        content = {
                            VehicleCard(vehicle = car)
                        },
                        enableDismissFromStartToEnd = false
                    )
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
        Toast.makeText(context, stringResource(id = R.string.car_added_successfully), Toast.LENGTH_SHORT).show()
        showAddToast = false
    }

    if (showDeleteToast) {
        Toast.makeText(context, stringResource(id = R.string.car_deleted_successfully), Toast.LENGTH_SHORT).show()
        showDeleteToast = false
    }
}

@Composable
fun VehicleCard(
    vehicle: Car,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = SpaceExtraSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(SpaceMedium)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_car_24),
                contentDescription = stringResource(id = R.string.car_icon_desc),
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = SpaceMedium)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = vehicle.brand, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(id = R.string.model_label, vehicle.model),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}