package com.example.parkpal.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import com.example.parkpal.ui.theme.*

@Composable
fun BottomNavigationBar(
    currentDestination: BottomNavDestination,
    onDestinationClicked: (BottomNavDestination) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceMedium, vertical = SpaceLarge),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(RadiusLarge))
                .background(MaterialTheme.colorScheme.surface)
                .shadow(elevation = ElevationExtraLarge, shape = RoundedCornerShape(RadiusLarge)),
            tonalElevation = ElevationSmall,
        ) {
            listOf(
                BottomNavDestination.MyCar,
                BottomNavDestination.ParkingHistory,
                BottomNavDestination.Account
            ).forEach { destination ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.icon),
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = currentDestination.route == destination.route,
                    onClick = { onDestinationClicked(destination) }
                )
            }
        }
    }
}