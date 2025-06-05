package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.parkpal.data.model.ParkingLocation

@Dao
interface ParkingLocationDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertParkingLocation(parkingLocation: ParkingLocation): Long

    @Update
    suspend fun updateParkingLocation(parkingLocation: ParkingLocation)

    @Delete
    suspend fun deleteParkingLocation(parkingLocation: ParkingLocation)
}