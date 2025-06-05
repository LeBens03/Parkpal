package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.parkpal.data.model.Car
import com.example.parkpal.data.model.ParkingHistory
import com.example.parkpal.data.model.ParkingLocation

@Dao
interface CarDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCar(car: Car): Long

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)

    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarById(carId: Int): Car

    @Transaction
    @Query("SELECT * FROM parking_location_table WHERE carId = :carId ")
    suspend fun getParkingLocation(carId: Int): ParkingLocation?

    @Transaction
    @Query("SELECT * FROM parking_history_table WHERE carId = :carId")
    suspend fun getParkingHistory(carId: Int): ParkingHistory?
}