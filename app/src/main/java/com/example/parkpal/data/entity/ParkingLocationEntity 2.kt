package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a parking location record for a car owned by a user.
 * Corresponds to the "parking_location_table" in the Room database.
 *
 * Foreign keys:
 * - References the owning user (UserEntity) via `userId`.
 * - References the parked car (CarEntity) via `carId`.
 *
 * Indices on `userId` and `carId` improve query performance.
 *
 * @property parkingLocationId The unique identifier for the parking location record (auto-generated).
 * @property userId The ID of the user who owns the car.
 * @property carId The ID of the car parked at this location.
 * @property latitude The latitude coordinate of the parking spot.
 * @property longitude The longitude coordinate of the parking spot.
 * @property address The human-readable address or description of the parking location.
 * @property timestamp The timestamp (in milliseconds) when the car was parked.
 * @property duration The duration (in milliseconds) the car is expected to remain parked.
 */
@Entity(
    tableName = "parking_location_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Deletes parking locations when the user is deleted
        ),
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE // Deletes parking locations when the car is deleted
        )
    ],
    indices = [Index("userId"), Index("carId")]
)
data class ParkingLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val parkingLocationId: Long = 0,
    val userId: Long,
    val carId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: Long,
    val duration: Long
)