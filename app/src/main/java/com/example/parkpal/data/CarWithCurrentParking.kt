package com.example.parkpal.data

import androidx.room.Embedded
import androidx.room.Relation

data class CarWithCurrentParking(
    @Embedded val car: Car,

    @Relation(
        parentColumn = "currentParkingLocationId",
        entityColumn = "id"
    )
    val currentParkingLocation: ParkingLocation?
)

