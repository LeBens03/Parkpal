package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ParkingHistoryDAO {
    @Transaction
    @Query("SELECT * FROM parking_history_table WHERE carId = :carId")
    suspend fun getParkingHistoryForCar(carId: Int): ParkingHistoryWithLocation
}
