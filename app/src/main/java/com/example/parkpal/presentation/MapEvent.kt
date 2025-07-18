package com.example.parkpal.presentation

import com.example.parkpal.domain.model.ParkingLocation

sealed class MapEvent {
    data class OnParkingLocationClicked(val parkingLocation: ParkingLocation) : MapEvent()
    data class OnParkMyCarClicked(val parkingLocation: ParkingLocation) : MapEvent()
}