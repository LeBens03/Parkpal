package com.example.parkpal.presentation

import com.example.parkpal.domain.model.ParkingLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = true,
    ),
    val userLocation: LatLng? = null,
    val parkingLocation: ParkingLocation? = null
)
