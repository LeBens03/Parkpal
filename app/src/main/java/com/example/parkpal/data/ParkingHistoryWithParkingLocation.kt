package com.example.parkpal.data

import androidx.room.Embedded
import androidx.room.Relation

data class ParkingHistoryWithLocation(
    @Embedded val parkingHistory: ParkingHistory,

    @Relation(
        parentColumn = "parkingLocationId",
        entityColumn = "id"
    )
    val parkingLocation: List<ParkingLocation>
)
