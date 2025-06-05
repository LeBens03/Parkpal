package com.example.parkpal.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "car_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.Companion.CASCADE
    ),
        ForeignKey(
            entity = ParkingLocation::class,
            parentColumns = ["parkingLocationId"],
            childColumns = ["currentParkingLocationId"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("currentParkingLocationId")]
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