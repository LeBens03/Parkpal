package com.example.parkpal.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithCarWithParkingHistory(
    @Embedded val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val cars: List<CarWithParkingHistory>
)

