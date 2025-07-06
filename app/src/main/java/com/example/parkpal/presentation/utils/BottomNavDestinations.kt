package com.example.parkpal.presentation.utils

import com.example.parkpal.R

/**
 * Represents destinations for the bottom navigation bar.
 *
 * @property route The navigation route string.
 * @property label The display label for the navigation item.
 * @property icon The drawable resource ID for the icon.
 */
sealed class BottomNavDestination(val route: String, val label: String, val icon: Int) {

    /** Destination for the "My Car" screen. */
    object MyCar : BottomNavDestination(
        route = "my_car",
        label = "My Car",
        icon = R.drawable.baseline_directions_car_24
    )

    /** Destination for the "Parking History" screen. */
    object ParkingHistory : BottomNavDestination(
        route = "parking_history",
        label = "Parking History",
        icon = R.drawable.baseline_assignment_24
    )

    /** Destination for the "Account" screen. */
    object Account : BottomNavDestination(
        route = "account",
        label = "Account",
        icon = R.drawable.baseline_account_circle_24
    )
}