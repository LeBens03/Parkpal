package com.example.parkpal.presentation.utils

import com.example.parkpal.domain.model.ParkingLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties

/**
 * Represents the state of the map UI.
 *
 * @property properties [MapProperties] defines various map settings such as enabling the user's location layer.
 * @property userLocation The current location of the user as a [LatLng]. `null` if not yet available.
 * @property parkingLocation The location where the car is parked. `null` if no parking location is set.
 */
data class MapState(
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = true,
    ),
    val userLocation: LatLng? = null,
    val parkingLocation: ParkingLocation? = null
)
