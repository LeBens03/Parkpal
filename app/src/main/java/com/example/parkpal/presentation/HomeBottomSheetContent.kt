package com.example.parkpal.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.parkpal.R
import java.util.Locale

@Composable
fun HomeBottomSheetContent(
    distance: Float?,
    address: String?,
    onNavigateClick: () -> Unit,
    onArrivedClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.parking_details),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val distanceText = distance?.let {
            String.format(Locale.getDefault(), "%.2f km", it)
        } ?: stringResource(id = R.string.unknown)

        Text(
            text = stringResource(id = R.string.distance_format, distanceText),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.address_format, address ?: stringResource(id = R.string.unknown)),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onNavigateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.navigate_with_google_maps))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onArrivedClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.i_arrived))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onShareClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.share_location))
        }
    }
}