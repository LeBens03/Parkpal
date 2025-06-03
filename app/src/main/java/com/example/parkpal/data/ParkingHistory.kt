package com.example.parkpal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_history_table",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParkingLocation::class,
            parentColumns = ["id"],
            childColumns = ["parkingLocationId"],
            onDelete = ForeignKey.CASCADE
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

