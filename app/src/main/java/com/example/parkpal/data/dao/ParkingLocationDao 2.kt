package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.parkpal.data.entity.ParkingLocationEntity

/**
 * Data Access Object (DAO) interface for managing ParkingLocationEntity records
 * in the database.
 */
@Dao
interface ParkingLocationDao {

    /**
     * Inserts a parking location record into the database.
     * If a record with the same primary key exists, it will be replaced.
     *
     * @param parkingLocation The ParkingLocationEntity to insert.
     * @return The row ID of the inserted parking location.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingLocation(parkingLocation: ParkingLocationEntity): Long

    /**
     * Deletes a parking location record from the database.
     *
     * @param parkingLocation The ParkingLocationEntity to delete.
     */
    @Delete
    suspend fun deleteParkingLocation(parkingLocation: ParkingLocationEntity)
}
