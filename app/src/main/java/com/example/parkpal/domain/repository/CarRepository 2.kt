package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.mapper.toCar
import com.example.parkpal.data.mapper.toCarEntity
import com.example.parkpal.domain.model.Car
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for managing car-related data operations.
 *
 * Provides an abstraction over CarDao to handle database operations
 * while mapping between database entities and domain models.
 *
 * @property carDao The DAO interface for car database operations.
 */
@Singleton
class CarRepository @Inject constructor(private val carDao: CarDao) {

    /**
     * Inserts a new car into the database.
     *
     * @param car The Car domain model to insert.
     * @return The row ID of the inserted car.
     */
    suspend fun insertCar(car: Car): Long {
        Log.d("CarRepository", "Insert car: $car")
        return carDao.insertCar(car.toCarEntity())
    }

    /**
     * Deletes a car from the database.
     *
     * @param car The Car domain model to delete.
     */
    suspend fun deleteCar(car: Car) {
        Log.d("CarRepository", "Delete car: $car")
        carDao.deleteCar(car.toCarEntity())
    }

    /**
     * Retrieves all cars associated with a given user ID.
     *
     * @param userId The ID of the user whose cars are requested.
     * @return A list of Car domain models owned by the user.
     */
    suspend fun getCarsByUserId(userId: Long): List<Car> {
        Log.d("CarRepository", "Get cars by userId: $userId")
        return carDao.getCarsByUserId(userId).map { it.toCar() }
    }

    /**
     * Retrieves all cars associated with a user's email.
     *
     * @param userEmail The email of the user whose cars are requested.
     * @return A list of Car domain models owned by the user.
     */
    suspend fun getCarByUserEmail(userEmail: String): List<Car> {
        Log.d("CarRepository", "Get cars by userEmail: $userEmail")
        return carDao.getCarsByUserEmail(userEmail).map { it.toCar() }
    }

    /**
     * Retrieves a single car by its unique ID.
     *
     * @param carId The unique identifier of the car.
     * @return The Car domain model if found, or null if not found.
     */
    suspend fun getCarById(carId: Long): Car? {
        Log.d("CarRepository", "Get car by id: $carId")
        return carDao.getCarById(carId)?.toCar()
    }
}