package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkpal.data.entity.ParkingHistoryEntity

/**
 * Data Access Object (DAO) interface for managing ParkingHistoryEntity records
 * in the database.
 */
@Dao
interface ParkingHistoryDao {

    /**
     * Inserts a parking history record into the database.
     * If a record with the same primary key exists, it will be replaced.
     *
     * @param parkingHistory The ParkingHistoryEntity to insert.
     * @return The row ID of the inserted parking history.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingHistory(parkingHistory: ParkingHistoryEntity): Long

    /**
     * Deletes parking history entries matching the specified user ID.
     *
     * @param userId The userId whose parking history entries will be deleted.
     */
    @Query("DELETE FROM parking_history_table WHERE userId = :userId")
    suspend fun deleteParkingHistoryById(userId: Long)

    /**
     * Retrieves the most recent parking history entry for a specific user,
     * ordered by descending parkingHistoryId to get the latest entry.
     *
     * @param userId The ID of the user whose latest parking history is requested.
     * @return The latest ParkingHistoryEntity for the user, or null if none exists.
     */
    @Query(
        "SELECT * FROM parking_history_table " +
                "WHERE userId = :userId " +
                "ORDER BY parkingHistoryId DESC LIMIT 1"
    )
    suspend fun getParkingHistoryByUserId(userId: Long): ParkingHistoryEntity?
}