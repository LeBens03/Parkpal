package com.example.parkpal.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_history_table",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = ParkingLocation::class,
            parentColumns = ["parkingLocationId"],
            childColumns = ["parkingLocationId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("carId"), Index("parkingLocationId")]
)
data class ParkingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val carId: Int,
    val parkingLocationId: Int
)