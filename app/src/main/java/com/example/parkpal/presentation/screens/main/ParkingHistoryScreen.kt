package com.example.parkpal.presentation.screens.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkpal.R
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
import com.example.parkpal.ui.theme.ElevationExtraLarge
import com.example.parkpal.ui.theme.RadiusMedium
import com.example.parkpal.ui.theme.Shapes
import com.example.parkpal.ui.theme.SpaceLarge
import com.example.parkpal.ui.theme.SpaceMedium
import com.example.parkpal.ui.theme.SpaceSmall
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun ParkingHistoryScreen(
    userId: Long,
    carViewModel: CarViewModel,
    parkingHistoryViewModel: ParkingHistoryViewModel
) {
    val parkingHistory by parkingHistoryViewModel.currentParkingHistory.collectAsState()
    val locations = parkingHistory?.parkingLocations ?: emptyList()
    val carIds = locations.map { it.carId }

    var showClearToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(userId) {
        parkingHistoryViewModel.fetchParkingHistory(userId)
    }

    LaunchedEffect(carIds) {
        if (carIds.isNotEmpty()) {
            carViewModel.fetchLicensePlatesForCars(carIds)
        }
    }

    Log.d("ParkingHistoryScreen", "Current userId: $userId")
    Log.d("ParkingHistoryScreen", "Parking history: $parkingHistory")

    if (locations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = stringResource(id = R.string.no_parking_history),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave room for BottomNav
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SpaceMedium)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SpaceMedium)
                ) {
                    items(locations, key = { it.timestamp }) { location ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    parkingHistoryViewModel.deleteParkingLocation(location)
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
                                        .padding(vertical = SpaceSmall)
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
                                ParkingLocationCard(location, carViewModel)
                            },
                            enableDismissFromStartToEnd = false
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp, end = 16.dp) // adjust padding as needed
                    .navigationBarsPadding(), // makes sure FAB floats above nav bar
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = {
                        parkingHistoryViewModel.clearParkingHistory(userId)
                        showClearToast = true
                    },
                    containerColor = MaterialTheme.colorScheme.error, // red to match delete
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(ElevationExtraLarge),
                    shape = RoundedCornerShape(50) // fully rounded
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_all_history)
                    )
                }
            }
        }

        if (showClearToast) {
            Toast.makeText(context, stringResource(id = R.string.parking_history_cleared), Toast.LENGTH_SHORT).show()
            showClearToast = false
        }
    }
}

@Composable
fun ParkingLocationCard(location: ParkingLocation, carViewModel: CarViewModel) {
    val licensePlates by carViewModel.licensePlates.collectAsState()
    val licensePlate = licensePlates[location.carId] ?: "Unknown"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpaceSmall),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(RadiusMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
        ) {
            Text(text = stringResource(id = R.string.address_label_formatted, location.address), style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(id = R.string.duration_label, formatDuration(location.duration)), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = stringResource(id = R.string.date_label, formatTimestamp(location.timestamp)), style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(id = R.string.car_plate_label, licensePlate), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun formatDuration(duration: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(duration)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
    return if (hours > 0) "$hours hrs $minutes mins" else "$minutes mins"
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return format.format(date)
}