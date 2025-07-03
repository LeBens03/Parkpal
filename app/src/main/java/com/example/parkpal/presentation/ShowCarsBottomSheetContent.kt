package com.example.parkpal.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkpal.R
import com.example.parkpal.domain.model.Car
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource

@Composable
fun ShowCarsBottomSheetContent(
    cars: List<Car>,
    onCarSelected: (Car) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (cars.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    contentDescription = stringResource(id = R.string.no_cars_icon_description),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.no_cars_found),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Text(
                text = stringResource(id = R.string.select_car_to_park),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(cars) { car ->
                    VehicleCard(
                        vehicle = car,
                        onClick = {
                            onCarSelected(car)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleCard(
    vehicle: Car,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_directions_car_24),
                contentDescription = stringResource(
                    id = R.string.car_icon_description,
                    vehicle.brand,
                    vehicle.model
                ),
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
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