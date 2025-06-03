package com.example.parkpal.data

import androidx.room.Embedded
import androidx.room.Relation

data class CarWithParkingHistory(
    @Embedded val car: Car,

    @Relation(
        parentColumn = "id",
        entityColumn = "carId"
    )
    val parkingHistory: ParkingHistoryWithLocation
)

