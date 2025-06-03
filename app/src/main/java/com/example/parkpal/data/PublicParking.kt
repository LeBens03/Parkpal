package com.example.parkpal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "public_parking_table")
data class PublicParking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val apiId: String,         // Unique ID from the API
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val isFavorite: Boolean = false
)
