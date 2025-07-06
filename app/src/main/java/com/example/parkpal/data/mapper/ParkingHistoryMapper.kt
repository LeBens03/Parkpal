package com.example.parkpal.data.mapper

import com.example.parkpal.domain.model.ParkingHistory
import com.example.parkpal.data.entity.ParkingHistoryEntity

/**
 * Converts a ParkingHistoryEntity (database layer) into a ParkingHistory (domain layer).
 *
 * This mapping isolates the domain model from database persistence.
 *
 * @receiver ParkingHistoryEntity The entity object retrieved from the database.
 * @return ParkingHistory The corresponding domain model object.
 */
fun ParkingHistoryEntity.toParkingHistory(): ParkingHistory {
    return ParkingHistory(
        userId = userId,
        parkingLocations = parkingLocations
    )
}

/**
 * Converts a ParkingHistory (domain layer) into a ParkingHistoryEntity (database layer).
 *
 * Prepares the domain object for persistence in the database.
 *
 * @receiver ParkingHistory The domain model object.
 * @return ParkingHistoryEntity The corresponding database entity.
 */
fun ParkingHistory.toParkingHistoryEntity(): ParkingHistoryEntity {
    return ParkingHistoryEntity(
        userId = userId,
        parkingLocations = parkingLocations
    )
}