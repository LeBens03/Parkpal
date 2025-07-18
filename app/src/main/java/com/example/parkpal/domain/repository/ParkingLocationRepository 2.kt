package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.mapper.toParkingLocationEntity
import com.example.parkpal.domain.model.ParkingLocation
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for managing parking location data operations.
 *
 * Provides an abstraction over ParkingLocationDao and handles conversion
 * between domain models and database entities.
 *
 * @property parkingLocationDao The DAO interface for parking location database operations.
 */
@Singleton
class ParkingLocationRepository @Inject constructor(
    private val parkingLocationDao: ParkingLocationDao
) {
    /**
     * Inserts a parking location into the database.
     *
     * @param parkingLocation The domain model representing the parking location to insert.
     */
    suspend fun insertParkingLocation(parkingLocation: ParkingLocation) {
        Log.d("ParkingLocationRepository", "Insert parking location: $parkingLocation")
        parkingLocationDao.insertParkingLocation(parkingLocation.toParkingLocationEntity())
    }

    /**
     * Deletes a parking location from the database.
     *
     * @param parkingLocation The domain model representing the parking location to delete.
     */
    suspend fun deleteParkingLocation(parkingLocation: ParkingLocation) {
        Log.d("ParkingLocationRepository", "Delete parking location: $parkingLocation")
        parkingLocationDao.deleteParkingLocation(parkingLocation.toParkingLocationEntity())
    }
}