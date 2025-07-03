package com.example.parkpal.presentation.screens.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.presentation.viewmodel.CarViewModel
import java.text.SimpleDateFormat
import java.util.Locale
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

    var showClearToast = remember { mutableStateOf(false) }
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
        Text(
            text = "No parking history found.",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Button(
                onClick = {
                    parkingHistoryViewModel.clearParkingHistory(userId = userId)
                    showClearToast.value = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete All History")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(locations) { location ->
                    ParkingLocationCard(location, parkingHistoryViewModel, carViewModel)
                }
            }
        }

        if (showClearToast.value) {
            Toast.makeText(context, "Parking history cleared", Toast.LENGTH_SHORT).show()
            showClearToast.value = false
        }
    }
}

@Composable
fun ParkingLocationCard(location: ParkingLocation, parkingHistoryViewModel: ParkingHistoryViewModel, carViewModel: CarViewModel) {

    val licensePlates by carViewModel.licensePlates.collectAsState()
    val licensePlate = licensePlates[location.carId] ?: "Unknown"

    var showDeleteToast = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Address: ${location.address}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Duration: ${formatDuration(location.duration)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Text(
                text = "Date: ${formatTimestamp(location.timestamp)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Car Plate: $licensePlate",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    parkingHistoryViewModel.deleteParkingLocation(location)
                    showDeleteToast.value = true
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Delete")
            }
        }
    }

    if (showDeleteToast.value) {
        Toast.makeText(context, "Parking location deleted", Toast.LENGTH_SHORT).show()
        showDeleteToast.value = false
    }
}

fun formatDuration(duration: Long): String {
    Log.d("ParkingLocationCard", "Duration: $duration")
    val hours = TimeUnit.MILLISECONDS.toHours(duration)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
    return if (hours > 0) {
        "$hours hrs $minutes mins"
    } else {
        "$minutes mins"
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return format.format(date)
}