package com.example.parkpal.presentation.utils

import com.example.parkpal.domain.model.ParkingLocation

/**
 * Represents user interactions related to the map, particularly for handling parking actions.
 */
sealed class MapEvent {

    /**
     * Event triggered when the user selects a previously saved parking location.
     *
     * @property parkingLocation The selected [ParkingLocation].
     */
    data class OnParkingLocationClicked(val parkingLocation: ParkingLocation) : MapEvent()

    /**
     * Event triggered when the user parks their car and chooses to save the current location.
     *
     * @property parkingLocation The [ParkingLocation] to be saved.
     */
    data class OnParkMyCarClicked(val parkingLocation: ParkingLocation) : MapEvent()
}