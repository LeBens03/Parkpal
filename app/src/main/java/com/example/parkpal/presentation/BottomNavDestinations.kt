package com.example.parkpal.presentation

import com.example.parkpal.R


sealed class BottomNavDestination(val route: String, val label: String, val icon: Int) {
    object MyCar : BottomNavDestination("my_car", "My Car", R.drawable.baseline_directions_car_24)
    object ParkingHistory : BottomNavDestination("parking_history", "Parking History", R.drawable.baseline_assignment_24 )
    object Account : BottomNavDestination("account", "Account", R.drawable.baseline_account_circle_24)
}
