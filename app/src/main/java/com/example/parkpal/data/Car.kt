package com.example.parkpal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "car_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = ParkingLocation::class,
            parentColumns = ["parkingId"],
            childColumns = ["currentParkingLocationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Car(
    @PrimaryKey(autoGenerate = true)
    val carId: Int = 0,
    val userId: Int, // Foreign key to User table
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val currentParkingLocationId: Int? = null // Foreign key to the current parking location
)
