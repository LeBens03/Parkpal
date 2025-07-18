package com.example.parkpal.domain.model

/**
 * Represents a user's parking history, storing multiple parking locations.
 *
 * Used in the domain/business layer to keep track of past parking spots.
 *
 * @property parkingHistoryId Unique identifier for this parking history record, default 0 for new entries.
 * @property userId The ID of the user this parking history belongs to.
 * @property parkingLocations A list of ParkingLocation objects representing past parking spots.
 */
data class ParkingHistory(
    val parkingHistoryId: Long = 0,
    val userId: Long,
    val parkingLocations: List<ParkingLocation>
)