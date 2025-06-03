package com.example.parkpal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "parking_location_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Car::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParkingLocation(
    @PrimaryKey(autoGenerate = true)
    val parkingId: Int = 0,
    val userId: Int, // Foreign key to User table
    val carId: Int, // Foreign key to Car table
    val latitude: Double,
    val longitude: Double,
    val timestamp: Date
)
