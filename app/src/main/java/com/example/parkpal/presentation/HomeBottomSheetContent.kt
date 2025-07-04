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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.parkpal.R
import com.example.parkpal.ui.theme.SpaceLarge
import com.example.parkpal.ui.theme.SpaceMedium
import com.example.parkpal.ui.theme.SpaceSmall
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
            .padding(SpaceLarge)
    ) {
        Text(
            text = stringResource(id = R.string.parking_details),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = SpaceMedium)
        )

        val distanceText = distance?.let {
            String.format(Locale.getDefault(), "%.2f km", it)
        } ?: stringResource(id = R.string.unknown)

        Text(
            text = stringResource(id = R.string.distance_format, distanceText),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = SpaceSmall)
        )

        Text(
            text = stringResource(
                id = R.string.address_format,
                address ?: stringResource(id = R.string.unknown)
            ),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = SpaceLarge)
        )

        // Button style abstraction
        val buttonModifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)

        Button(
            onClick = onNavigateClick,
            modifier = buttonModifier
        ) {
            Text(
                text = stringResource(id = R.string.navigate_with_google_maps),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        Button(
            onClick = onArrivedClick,
            modifier = buttonModifier
        ) {
            Text(
                text = stringResource(id = R.string.i_arrived),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(SpaceMedium))

        Button(
            onClick = onShareClick,
            modifier = buttonModifier
        ) {
            Text(
                text = stringResource(id = R.string.share_location),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}