package com.example.parkpal.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPublicParking(
    @Embedded val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val favoritePublicParking: List<PublicParking>
)
