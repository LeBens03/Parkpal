package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.ParkingHistoryDao
import com.example.parkpal.data.mapper.toParkingHistory
import com.example.parkpal.data.mapper.toParkingHistoryEntity
import com.example.parkpal.domain.model.ParkingHistory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for managing parking history data operations.
 *
 * Acts as an abstraction layer over ParkingHistoryDao, handling
 * conversions between domain models and database entities.
 *
 * @property parkingHistoryDao The DAO interface for parking history database operations.
 */
@Singleton
class ParkingHistoryRepository @Inject constructor(
    private val parkingHistoryDao: ParkingHistoryDao
) {
    /**
     * Inserts a parking history record into the database.
     *
     * @param parkingHistory The domain model representing the parking history to insert.
     */
    suspend fun insertParkingHistory(parkingHistory: ParkingHistory) {
        Log.d("ParkingHistoryRepository", "Insert parking history: $parkingHistory")
        parkingHistoryDao.insertParkingHistory(parkingHistory.toParkingHistoryEntity())
    }

    /**
     * Deletes a parking history record by the user's ID.
     *
     * @param userId The ID of the user whose parking history should be deleted.
     */
    suspend fun deleteParkingHistoryById(userId: Long) {
        Log.d("ParkingHistoryRepository", "Delete parking history by user ID: $userId")
        parkingHistoryDao.deleteParkingHistoryById(userId)
    }

    /**
     * Retrieves the latest parking history for a given user ID.
     *
     * @param userId The ID of the user whose parking history is requested.
     * @return The ParkingHistory domain model if found, or null otherwise.
     */
    suspend fun getParkingHistoryByUserId(userId: Long): ParkingHistory? {
        Log.d("ParkingHistoryRepository", "Get parking history by user ID: $userId")
        return parkingHistoryDao.getParkingHistoryByUserId(userId)?.toParkingHistory()
    }
}