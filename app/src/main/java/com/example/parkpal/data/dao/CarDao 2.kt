package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.data.entity.ParkingLocationEntity

/**
 * Data Access Object (DAO) interface for performing database operations on CarEntity
 * and related parking location data.
 */
@Dao
interface CarDao {

    /**
     * Inserts a new car record into the database.
     * If a car with the same primary key exists, it will be replaced.
     *
     * @param car The CarEntity object to insert.
     * @return The row ID of the inserted car.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarEntity): Long

    /**
     * Updates an existing car record in the database.
     * Matches the car by primary key.
     *
     * @param car The CarEntity object with updated data.
     */
    @Update
    suspend fun updateCar(car: CarEntity)

    /**
     * Deletes a car record from the database.
     *
     * @param car The CarEntity object to delete.
     */
    @Delete
    suspend fun deleteCar(car: CarEntity)

    /**
     * Retrieves a list of cars associated with a specific user ID.
     *
     * @param userId The ID of the user whose cars are to be retrieved.
     * @return List of CarEntity objects owned by the user.
     */
    @Query("SELECT * FROM car_table WHERE userId = :userId")
    suspend fun getCarsByUserId(userId: Long): List<CarEntity>

    /**
     * Retrieves a list of cars associated with a user's email.
     * Uses a join between car_table and user_table.
     * The annotation @RewriteQueriesToDropUnusedColumns optimizes the query by removing unused columns.
     *
     * @param userEmail The email of the user whose cars are to be retrieved.
     * @return List of CarEntity objects owned by the user.
     */
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT car_table.* FROM car_table
        INNER JOIN user_table ON car_table.userId = user_table.userId
        WHERE user_table.email = :userEmail
    """)
    suspend fun getCarsByUserEmail(userEmail: String): List<CarEntity>

    /**
     * Retrieves a single car by its car ID.
     *
     * @param carId The ID of the car to retrieve.
     * @return The CarEntity object if found, or null if not found.
     */
    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarById(carId: Long): CarEntity?

    /**
     * Retrieves the parking location associated with a given car ID.
     * The @Transaction annotation ensures the query is run in a single database transaction.
     *
     * @param carId The ID of the car whose parking location is requested.
     * @return The ParkingLocationEntity associated with the car.
     */
    @Transaction
    @Query("SELECT * FROM parking_location_table WHERE carId = :carId")
    suspend fun getParkingLocation(carId: Long): ParkingLocationEntity
}