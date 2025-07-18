package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.parkpal.data.mapper.ParkingLocationConverter
import com.example.parkpal.domain.model.ParkingLocation

/**
 * Represents a user's parking history, storing multiple parking locations.
 * Corresponds to the "parking_history_table" in the Room database.
 *
 * Foreign key:
 * - References the owning user (UserEntity) via `userId`.
 *
 * Index on `userId` improves query performance.
 *
 * Uses a TypeConverter to persist a list of ParkingLocation domain models
 * as a database-supported format (e.g., JSON string).
 *
 * @property parkingHistoryId The unique identifier for this parking history record (auto-generated).
 * @property userId The ID of the user to whom this parking history belongs.
 * @property parkingLocations The list of parking locations saved in this history entry.
 */
@Entity(
    tableName = "parking_history_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Deletes history when user is deleted
        )
    ],
    indices = [Index("userId")]
)
@TypeConverters(ParkingLocationConverter::class)
data class ParkingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val parkingHistoryId: Long = 0,
    val userId: Long,
    val parkingLocations: List<ParkingLocation>
)