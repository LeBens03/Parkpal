package com.example.parkpal.domain.model

/**
 * Represents a parking location where a user's car is parked.
 *
 * Used in the domain/business layer for app logic and UI representation.
 *
 * @property parkingLocationId Unique identifier for the parking location, default 0 for new entries.
 * @property userId The ID of the user who owns the car.
 * @property carId The ID of the car parked at this location.
 * @property latitude The geographic latitude coordinate of the parking spot.
 * @property longitude The geographic longitude coordinate of the parking spot.
 * @property address Human-readable address or description of the parking location.
 * @property timestamp The time (in milliseconds) when the car was parked.
 * @property duration The expected duration (in milliseconds) the car will remain parked.
 */
data class ParkingLocation(
    val parkingLocationId: Long = 0,
    val userId: Long,
    val carId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: Long,
    val duration: Long
)