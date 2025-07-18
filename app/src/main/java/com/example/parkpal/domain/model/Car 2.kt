package com.example.parkpal.domain.model

/**
 * Represents a car owned by a user in the domain/business layer.
 *
 * Used throughout the app for business logic and UI representation,
 * separate from database persistence concerns.
 *
 * @property carId Unique identifier for the car, default 0 for new cars.
 * @property userId The ID of the user who owns this car.
 * @property brand The manufacturer brand of the car (e.g., "Toyota").
 * @property model The specific model of the car (e.g., "Corolla").
 * @property year The manufacturing year of the car.
 * @property licensePlate The license plate number.
 * @property currentParkingLocationId Optional ID referencing the current parking location.
 */
data class Car(
    val carId: Long = 0,
    val userId: Long,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val currentParkingLocationId: Int? = null
)