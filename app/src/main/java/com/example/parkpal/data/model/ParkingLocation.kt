package com.example.parkpal.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_location_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.Companion.CASCADE
    ),
        ForeignKey(
            entity = Car::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("userId"), Index("carId")]
)
data class ParkingLocation(
    @PrimaryKey(autoGenerate = true)
    val parkingLocationId: Int = 0,
    val userId: Int, // Foreign key to User table
    val carId: Int, // Foreign key to Car table
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)