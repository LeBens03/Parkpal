package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ParkingLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingLocation(parkingLocation: ParkingLocation): Long

    @Update
    suspend fun updateParkingLocation(parkingLocation: ParkingLocation)

    @Delete
    suspend fun deleteParkingLocation(parkingLocation: ParkingLocation)

    @Query("SELECT * FROM parking_location_table WHERE parkingId = :parkingId")
    suspend fun getParkingLocationById(parkingId: Int): ParkingLocation?

    @Transaction
    @Query("SELECT * FROM parking_location_table WHERE carId = :carId")
    suspend fun getParkingLocationForCar(carId: Int): ParkingLocation
}