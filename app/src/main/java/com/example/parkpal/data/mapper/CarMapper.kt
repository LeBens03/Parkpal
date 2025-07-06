package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.domain.model.Car

/**
 * Converts a CarEntity (database layer) into a Car (domain layer).
 *
 * This mapping helps to separate persistence details from business logic.
 *
 * @receiver CarEntity The entity object retrieved from the database.
 * @return Car The corresponding domain model object.
 */
fun CarEntity.toCar(): Car {
    return Car(
        carId = carId,
        userId = userId,
        brand = brand,
        model = model,
        year = year,
        licensePlate = licensePlate,
        currentParkingLocationId = currentParkingLocationId
    )
}

/**
 * Converts a Car (domain layer) into a CarEntity (database layer).
 *
 * This prepares the domain object for storage in the database.
 *
 * @receiver Car The domain model object.
 * @return CarEntity The corresponding database entity.
 */
fun Car.toCarEntity(): CarEntity {
    return CarEntity(
        carId = carId,
        userId = userId,
        brand = brand,
        model = model,
        year = year,
        licensePlate = licensePlate,
        currentParkingLocationId = currentParkingLocationId
    )
}