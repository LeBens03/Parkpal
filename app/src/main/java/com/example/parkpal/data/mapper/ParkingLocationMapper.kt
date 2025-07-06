package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.ParkingLocationEntity
import com.example.parkpal.domain.model.ParkingLocation

/**
 * Converts a ParkingLocationEntity (database layer) into a ParkingLocation (domain layer).
 *
 * This mapping separates the persistence representation from the app's business logic.
 *
 * @receiver ParkingLocationEntity The entity fetched from the database.
 * @return ParkingLocation The corresponding domain model object.
 */
fun ParkingLocationEntity.toParkingLocation(): ParkingLocation {
    return ParkingLocation(
        parkingLocationId = parkingLocationId,
        userId = userId,
        carId = carId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,
        address = address,
        duration = duration
    )
}

/**
 * Converts a ParkingLocation (domain layer) into a ParkingLocationEntity (database layer).
 *
 * This prepares the domain model for database storage.
 *
 * @receiver ParkingLocation The domain model object.
 * @return ParkingLocationEntity The corresponding database entity.
 */
fun ParkingLocation.toParkingLocationEntity(): ParkingLocationEntity {
    return ParkingLocationEntity(
        parkingLocationId = parkingLocationId,
        userId = userId,
        carId = carId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,
        address = address,
        duration = duration
    )
}