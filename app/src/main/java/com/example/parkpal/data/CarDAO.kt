package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface CarDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car): Long

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)

    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarById(carId: Int): Car

    @Transaction
    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarWithCurrentParkingLocation(carId: Int): CarWithCurrentParking?

    @Transaction
    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarWithParkingHistory(carId: Int): CarWithParkingHistory?
}