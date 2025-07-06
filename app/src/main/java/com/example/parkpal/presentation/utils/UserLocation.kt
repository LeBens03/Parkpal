package com.example.parkpal.presentation.utils

import android.location.Location
import kotlinx.coroutines.flow.Flow

/**
 * Interface to abstract user's real-time location updates.
 */
interface UserLocation {

    /**
     * Starts emitting location updates at the given interval.
     *
     * @param interval Time in milliseconds between location updates.
     * @return A [Flow] that emits [Location] objects as they are received.
     * @throws LocationException if the location service is unavailable or permission is denied.
     */
    fun getLocationUpdates(interval: Long): Flow<Location>

    /**
     * Exception thrown when the location cannot be retrieved.
     *
     * @param message Error message explaining the issue.
     */
    class LocationException(message: String) : Exception(message)
}