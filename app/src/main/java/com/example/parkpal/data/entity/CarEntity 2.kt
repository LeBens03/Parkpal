package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a car owned by a user in the application.
 * This entity corresponds to the "car_table" in the Room database.
 *
 * Foreign keys:
 * - References the owning user (UserEntity) via `userId`.
 * - Optionally references the current parking location (ParkingLocationEntity) via `currentParkingLocationId`.
 *
 * Indices on `userId` and `currentParkingLocationId` improve query performance.
 *
 * @property carId The unique identifier for the car (auto-generated primary key).
 * @property userId The ID of the user who owns this car.
 * @property brand The car manufacturer brand (e.g., "Toyota").
 * @property model The specific model of the car (e.g., "Corolla").
 * @property year The manufacturing year of the car.
 * @property licensePlate The car's license plate number.
 * @property currentParkingLocationId The optional ID of the current parking location where the car is parked.
 */
@Entity(
    tableName = "car_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Deletes cars when the user is deleted
        ),
        ForeignKey(
            entity = ParkingLocationEntity::class,
            parentColumns = ["parkingLocationId"],
            childColumns = ["currentParkingLocationId"],
            onDelete = ForeignKey.SET_NULL // Sets parking location to null if deleted
        )
    ],
    indices = [Index("userId"), Index("currentParkingLocationId")]
)
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val carId: Long = 0,
    val userId: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val currentParkingLocationId: Int? = null
)