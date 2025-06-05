package com.example.parkpal.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_public_parking_table",
    primaryKeys = ["userId", "publicParkingId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"]),
        ForeignKey(
            entity = PublicParking::class,
            parentColumns = ["publicParkingId"],
            childColumns = ["publicParkingId"]
        )
    ],
    indices = [Index(value = ["userId"]), Index(value = ["publicParkingId"])]
)
data class UserPublicParking(
    val userId: Int,
    val publicParkingId: Int
)